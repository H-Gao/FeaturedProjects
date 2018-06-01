"""Assignment 2 - Blocky

=== CSC148 Fall 2017 ===
Diane Horton and David Liu
Department of Computer Science,
University of Toronto


=== Module Description ===

This file contains the Goal class hierarchy.
"""

from typing import List, Tuple
from block import Block


class Goal:
    """A player goal in the game of Blocky.

    This is an abstract class. Only child classes should be instantiated.

    === Attributes ===
    colour:
        The target colour for this goal, that is the colour to which
        this goal applies.
    """
    colour: Tuple[int, int, int]

    def __init__(self, target_colour: Tuple[int, int, int]) -> None:
        """Initialize this goal to have the given target colour.
        """
        self.colour = target_colour

    def score(self, board: Block) -> int:
        """Return the current score for this goal on the given board.

        The score is always greater than or equal to 0.
        """
        raise NotImplementedError

    def description(self) -> str:
        """Return a description of this goal.
        """
        raise NotImplementedError


class BlobGoal(Goal):
    """A goal to create the largest connected blob of this goal's target
    colour, anywhere within the Block.

    === Attributes ===
    colour:
        The target colour for this goal, that is the colour to which
        this goal applies.
    """

    def __init__(self, target_colour: Tuple[int, int, int]) -> None:
        """Initialize this goal to have the given target colour.
        """
        Goal.__init__(self, target_colour)

    def score(self, board: Block) -> int:
        """Return the current score for this goal on the given board.

        The score is always greater than or equal to 0.
        """

        # Stores a 2D array representation of the board
        board_rep = board.flatten()

        # Stores a 2D array of -1, 0 or 1s, representing if it's unvisited,
        # visited and not the right colour or visited and the right colour.
        visited = []

        # Fills the 2D array with -1 (unvisited)
        for x in range(len(board_rep)):
            visited.append([])
            for y in range(len(board_rep)):
                visited[x].append(-1)

        # Stores the maximum blob score
        max_val = -1 # Default is -1, since score >= 0 > -1

        # Loops through each element of the board representation, and
        # checks to see if it's unvisited and in a blob.
        for x in range(len(board_rep)):
            for y in range(len(board_rep)):
                # Stores the score of the blob (or 0, if its not a blob of
                # this goal's colour)
                new_val = \
                    self._undiscovered_blob_size((x, y), board_rep, visited)

                # If the new score is greater, store the new score as max.
                if new_val > max_val:
                    max_val = new_val

        # Return the maximum score.
        return max_val

    def description(self) -> str:
        """Return a description of this goal.
        """
        return "Make the largest group of side-connected cells of your colour."

    def _undiscovered_blob_size(self, pos: Tuple[int, int],
                                board: List[List[Tuple[int, int, int]]],
                                visited: List[List[int]]) -> int:
        """Return the size of the largest connected blob that (a) is of this
        Goal's target colour, (b) includes the cell at <pos>, and (c) involves
        only cells that have never been visited.

        If <pos> is out of bounds for <board>, return 0.

        <board> is the flattened board on which to search for the blob.
        <visited> is a parallel structure that, in each cell, contains:
           -1  if this cell has never been visited
            0  if this cell has been visited and discovered
               not to be of the target colour
            1  if this cell has been visited and discovered
               to be of the target colour

        Update <visited> so that all cells that are visited are marked with
        either 0 or 1.
        """
        # Stores the x and y position (of this recursive call)
        x = pos[0]
        y = pos[1]

        # Stores whether or not the x and y positions are inside the 2D array
        in_bounds = 0 <= x < len(board) and 0 <= y < len(board)

        if in_bounds and visited[x][y] == -1:
            # If the x and y positions are in bounds and unvisited.

            if self.colour == board[x][y]:
                # If the block at (x, y) is the desired colour.
                visited[x][y] = 1

                # Stores the score of the blocks above, below, to the right and
                # left of this blob.
                up = self._undiscovered_blob_size((x, y-1), board, visited)
                down = self._undiscovered_blob_size((x, y + 1), board, visited)
                left = self._undiscovered_blob_size((x-1, y), board, visited)
                right = self._undiscovered_blob_size((x+1, y), board, visited)

                # Returns scores of the adjacent blocks, plus one (because
                # this block counts as a score).
                return up + down + left + right + 1
            else: # self.colour != board[x][y]
                # If the block at (x, y) is not of the desired colour.
                visited[x][y] = 0

        # If this x, y position are not in bounds, already visited or not
        # of the correct colour, return 0 (these blocks do not count as score)
        return 0


class PerimeterGoal(Goal):
    """A goal to create the most cells of this goal's target colour, around the
    perimeter of the board.

    === Attributes ===
    colour:
        The target colour for this goal, that is the colour to which
        this goal applies.
    """

    def __init__(self, target_colour: Tuple[int, int, int]) -> None:
        """Initialize this goal to have the given target colour.
        """
        Goal.__init__(self, target_colour)

    def score(self, board: Block) -> int:
        """Return the current score for this goal on the given board.

        The score is always greater than or equal to 0.
        """
        # Stores the score of the player (0 by default - increases later)
        player_score = 0

        # Stores the 2D representation of the board and its width/length.
        board_rep = board.flatten()
        board_len = len(board_rep)

        # SPECIAL CASE: If the board if only 1 block, then it shouldn't get
        # counted 4x. It should only get counted twice, because it's a corner.
        if board_len == 1 and board_rep[0][0] == self.colour:
            return 2
        # Otherwise, add up all of the score from the blocks in the outermost
        # rows and columns of the board. This is the total player score.
        # Note: Corners will be successfully double counted.
        else:
            player_score += self._find_line_score(board_rep, 0, 0, 0)
            player_score += self._find_line_score(board_rep, 0, 0, 1)
            player_score += self._find_line_score(board_rep, 0, board_len-1, 0)
            player_score += self._find_line_score(board_rep, board_len-1, 0, 1)

            # Returns the player score.
            return player_score

    def _find_line_score(self, board_rep: List[List[Tuple[int, int, int]]],
                         x_offset: int, y_offset: int, direction: int) -> int:
        """
        Searches a row/column of a 2D array (of colours) with the top, left
        corner at (x_offset, y_offset) and:
            1. Searches a row if direction == 0
            2. Searches a column if direction == 1
        and return the number of colours that match the goal colour - the score

        Precondition: direction is 0 (to search a row), direction is 1
        to search a column. x_offset, y_offset is within the bounds of board_rep
        """

        # Stores the score (number of colours matching the goal colour)
        score = 0

        # Loops for EACH colour in the specified row/column.
        for i in range(len(board_rep)):
            # Stores the <i>th element of the row (if direction == 0) OR the
            # column (if direction == 1).
            if direction == 0:
                curr_block_colour = board_rep[x_offset + i][y_offset]
            else: # direction == 1
                curr_block_colour = board_rep[x_offset][y_offset + i]

            # If the colour of the <i>th element matches the goal colour
            # Then, increase the score by 1.
            if curr_block_colour == self.colour:
                score += 1

        # Returns the score.
        return score

    def description(self) -> str:
        """Return a description of this goal.
        """
        return "Get the most cells of your colour around the board's perimeter"


if __name__ == '__main__':
    import python_ta
    python_ta.check_all(config={
        'allowed-import-modules': [
            'doctest', 'python_ta', 'random', 'typing',
            'block', 'goal', 'player', 'renderer'
        ],
        'max-attributes': 15
    })
