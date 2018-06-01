"""Assignment 2 - Blocky

=== CSC148 Fall 2017 ===
Diane Horton and David Liu
Department of Computer Science,
University of Toronto


=== Module Description ===

This file contains the player class hierarchy.
"""

import random
from typing import Optional, Tuple
import pygame
from renderer import Renderer
from block import Block
from goal import Goal

TIME_DELAY = 600


class Player:
    """A player in the Blocky game.

    This is an abstract class. Only child classes should be instantiated.

    === Public Attributes ===
    renderer:
        The object that draws our Blocky board on the screen
        and tracks user interactions with the Blocky board.
    id:
        This player's number.  Used by the renderer to refer to the player,
        for example as "Player 2"
    goal:
        This player's assigned goal for the game.
    """
    renderer: Renderer
    id: int
    goal: Goal

    def __init__(self, renderer: Renderer, player_id: int, goal: Goal) -> None:
        """Initialize this Player.
        """
        self.goal = goal
        self.renderer = renderer
        self.id = player_id

    def make_move(self, board: Block) -> int:
        """Choose a move to make on the given board, and apply it, mutating
        the Board as appropriate.

        Return 0 upon successful completion of a move, and 1 upon a QUIT event.
        """
        raise NotImplementedError


class HumanPlayer(Player):
    """A human player.

    A HumanPlayer can do a limited number of smashes.

    === Public Attributes ===
    num_smashes:
        number of smashes which this HumanPlayer has performed
    === Representation Invariants ===
    num_smashes >= 0
    """
    # === Private Attributes ===
    # _selected_block
    #     The Block that the user has most recently selected for action;
    #     changes upon movement of the cursor and use of arrow keys
    #     to select desired level.
    # _level:
    #     The level of the Block that the user selected
    #
    # == Representation Invariants concerning the private attributes ==
    #     _level >= 0

    # The total number of 'smash' moves a HumanPlayer can make during a game.
    MAX_SMASHES = 1

    num_smashes: int
    _selected_block: Optional[Block]
    _level: int

    def __init__(self, renderer: Renderer, player_id: int, goal: Goal) -> None:
        """Initialize this HumanPlayer with the given <renderer>, <player_id>
        and <goal>.
        """
        super().__init__(renderer, player_id, goal)
        self.num_smashes = 0

        # This HumanPlayer has done no smashes yet.
        # This HumanPlayer has not yet selected a block, so set _level to 0
        # and _selected_block to None.
        self._level = 0
        self._selected_block = None

    def process_event(self, board: Block,
                      event: pygame.event.Event) -> Optional[int]:
        """Process the given pygame <event>.

        Identify the selected block and mark it as highlighted.  Then identify
        what it is that <event> indicates needs to happen to <board>
        and do it.

        Return
           - None if <event> was not a board-changing move (that is, if was
             a change in cursor position, or a change in _level made via
            the arrow keys),
           - 1 if <event> was a successful move, and
           - 0 if <event> was an unsuccessful move (for example in the case of
             trying to smash in an invalid location or when the player is not
             allowed further smashes).
        """
        # Get the new "selected" block from the position of the cursor
        block = board.get_selected_block(pygame.mouse.get_pos(), self._level)

        # Remove the highlighting from the old "_selected_block"
        # before highlighting the new one
        if self._selected_block is not None:
            self._selected_block.highlighted = False
        self._selected_block = block
        self._selected_block.highlighted = True

        # Since get_selected_block may have not returned the block at
        # the requested level (due to the level being too low in the tree),
        # set the _level attribute to reflect the level of the block which
        # was actually returned.
        self._level = block.level

        if event.type == pygame.MOUSEBUTTONDOWN:
            block.rotate(event.button)
            return 1
        elif event.type == pygame.KEYDOWN:
            if event.key == pygame.K_UP:
                if block.parent is not None:
                    self._level -= 1
                return None

            elif event.key == pygame.K_DOWN:
                if len(block.children) != 0:
                    self._level += 1
                return None

            elif event.key == pygame.K_h:
                block.swap(0)
                return 1

            elif event.key == pygame.K_v:
                block.swap(1)
                return 1

            elif event.key == pygame.K_s:
                if self.num_smashes >= self.MAX_SMASHES:
                    print('Can\'t smash again!')
                    return 0
                if block.smash():
                    self.num_smashes += 1
                    return 1
                else:
                    print('Tried to smash at an invalid depth!')
                    return 0

    def make_move(self, board: Block) -> int:
        """Choose a move to make on the given board, and apply it, mutating
        the Board as appropriate.

        Return 0 upon successful completion of a move, and 1 upon a QUIT event.

        This method will hold focus until a valid move is performed.
        """
        self._level = 0
        self._selected_block = board

        # Remove all previous events from the queue in case the other players
        # have added events to the queue accidentally.
        pygame.event.clear()

        # Keep checking the moves performed by the player until a valid move
        # has been completed. Draw the board on every loop to draw the
        # selected block properly on screen.
        while True:
            self.renderer.draw(board, self.id)
            # loop through all of the events within the event queue
            # (all pending events from the user input)
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    return 1

                result = self.process_event(board, event)
                self.renderer.draw(board, self.id)
                if result is not None and result > 0:
                    # un-highlight the selected block
                    self._selected_block.highlighted = False
                    return 0


class RandomPlayer(Player):
    """A random player.

    This player makes random moves, on random blocks.

    === ATTRIBUTES ===
    renderer:
        The object that draws our Blocky board on the screen
        and tracks user interactions with the Blocky board.
    id:
        This player's number.  Used by the renderer to refer to the player,
        for example as "Player 2"
    goal:
        This player's assigned goal for the game."""

    def make_move(self, board: Block) -> int:
        """Choose a move to make on the given board, and apply it, mutating
        the Board as appropriate.

        Return 0 upon successful completion of a move, and 1 upon a QUIT event.
        """
        # Randomly choose a block, and highlight it.
        selected_block = _select_random_block(board)
        selected_block.highlighted = True

        # Draw the board and wait 600 ms
        self.renderer.draw(board, self.id)
        pygame.time.wait(TIME_DELAY)

        # Randomly chooses a move id, representing one of five moves.
        # and applies the represented move on the block (if possible).
        move_id = random.randint(0, 4)
        execute_selected_move(move_id, selected_block)

        # Un-highlight the block and draw the board.
        selected_block.highlighted = False
        self.renderer.draw(board, self.id)

        # Returns that it the turn is over (no QUIT).
        return 0


class SmartPlayer(Player):
    """A smart player.

    A player that looks at several possible moves and selects the best one.

    === Attributes ===
    _num_moves:
        an integer representing the number of moves this player
        looks ahead when choosing the best move to make.
    renderer:
        The object that draws our Blocky board on the screen
        and tracks user interactions with the Blocky board.
    id:
        This player's number.  Used by the renderer to refer to the player,
        for example as "Player 2"
    goal:
        This player's assigned goal for the game.

    === Representation Invariants ===
    5 <= num_moves <= 150

    The representation invariants from RandomPlayer"""
    _num_moves: int

    def __init__(self, difficulty: int, renderer: Renderer, player_id: int,
                 goal: Goal) -> None:
        """Initialize this Player.
        """
        # Initializes all instance attributes of Player.
        Player.__init__(self, renderer, player_id, goal)

        # == Implementation Note: ==
        # Difficulty is unchanging (according to the specs), and is
        # irrelevant to anything other than the number of moves.
        # So, only the number of moves is stored as an instance attribute
        # difficulty is only used to calculate the number of moves.

        # Stores the number of moves that the AI will look ahead
        # when selecting a move.
        self._num_moves = _get_num_moves(difficulty)

    def make_move(self, board: Block) -> int:
        """Choose a move to make on the given board, and apply it, mutating
        the Board as appropriate.

        Return 0 upon successful completion of a move, and 1 upon a QUIT event.
        """
        # Stores the best move, and extract/store the
        # best move type and best block to apply it on
        best_move = self._find_best_move(board)
        best_block = best_move[0]
        best_action = best_move[1]

        # Highlight the best block, and draw the board. Wait 600 ms.
        best_block.highlighted = True
        self.renderer.draw(board, self.id)
        pygame.time.wait(TIME_DELAY)

        # Executes the best move on the best block
        # (stores if it was successful or not).
        execute_selected_move(best_action, best_block)

        # Stops highlighting the selected block, and draw the board.
        best_block.highlighted = False
        self.renderer.draw(board, self.id)

        # Returns that it the turn is over (no QUIT).
        return 0

    def _find_best_move(self, board: 'Board') -> Tuple['Block', int]:
        """Looks ahead a certain number of moves and returns a tuple
        of (THE BEST BLOCK, THE BEST ACTION)"""

        # Stores the best score, block and action id.
        # Don't worry about default values - they WILL be changed soon
        best_score = -1
        best_block = None
        best_action = -1

        # This loop tests <num_moves> moves and evaluates the score
        # to find the best one.
        for _ in range(self._num_moves):
            # Picks a random node, and one of 4 actions represented by an
            # integer from 0 - 3 (inclusively) => Excludes '4' the smash move.
            test_block = _select_random_block(board)
            test_action = random.randint(0, 3)

            # Tests the outcome of the action on node.
            new_score = self._get_tested_score(board, test_block, test_action)

            # If the new move results in a higher score, store it!
            if new_score > best_score:
                best_score = new_score
                best_block = test_block
                best_action = test_action

        # Returns the tuple of the best block and action.
        return (best_block, best_action)

    def _get_tested_score(self, board: 'Block', curr_block: 'Block',
                          curr_action: int) -> int:
        """
        Returns the score IF the specified action (representing one of
        four moves - excludes smash) is applied on curr_block
        """
        # Performs the specified action (don't worry it will be reversed later).
        if curr_action == 0: # 0 represents a CW rotation
            curr_block.rotate(1)
        elif curr_action == 1: # 1 represents a CCW rotation
            curr_block.rotate(3)
        elif curr_action == 2: # 2 represents a vertical swap
            curr_block.swap(1)
        else: # 3 represents a horizontal swap
            curr_block.swap(0)

        # Evaluates the board AFTER the move is performed - stores the score.
        new_score = self.goal.score(board)

        # Undoes the specified action
        if curr_action == 0: # 0 represents a CW rotation
            curr_block.rotate(3)
        elif curr_action == 1: # 1 represents a CCW rotation
            curr_block.rotate(1)
        elif curr_action == 2: # 2 represents a vertical swap
            curr_block.swap(1)
        else: # 3 represents a horizontal swap
            curr_block.swap(0)

        # Returns the new score
        return new_score


def execute_selected_move(move_id: int, selected_block: 'Block')\
        -> None:
    """
    Applies the specified move on the specified block.

    move_id: Integer that represents one of five moves shown below.
    selected_block: The block to apply the move to.
    """

    # Apply the selected move on the selected block.
    if move_id == 0:
        selected_block.rotate(1)
    elif move_id == 1:
        selected_block.rotate(3)
    elif move_id == 2:
        selected_block.swap(1)
    elif move_id == 3:
        selected_block.swap(0)
    else: # move_id == 4
        # Only executes smash on a block that is NOT max or min depth.
        if 0 < selected_block.level < selected_block.max_depth:
            selected_block.smash()


def _select_random_block(board: Block) -> 'Block':
    """
    Returns a random block on the board.

    board: The board (root block)
    """
    # Gets a random level between 0 (the whole board) and max depth (the
    # smallest block).
    rand_level = random.randint(0, board.max_depth)

    # Gets a random x, y position on the board.
    rand_x = random.randint(0, board.size - 1)
    rand_y = random.randint(0, board.size - 1)

    # Returns the block at the random (x, y) position and level.
    return board.get_selected_block((rand_x, rand_y), rand_level)


def _get_num_moves(difficulty: int) -> int:
    """
    Returns the number of moves to look ahead, depending on the difficulty
    level of the player.

    Ie. Maps the difficulty to the number of moves made.
    """

    # Returns the number of moves to look ahead, depending on the difficulty
    # These values are all directly taken from specifications.
    if difficulty == 0:
        return 5
    elif difficulty == 1:
        return 10
    elif difficulty == 2:
        return 25
    elif difficulty == 3:
        return 50
    elif difficulty == 4:
        return 100
    else:
        return 150


if __name__ == '__main__':
    import python_ta
    python_ta.check_all(config={
        'allowed-io': ['process_event'],
        'allowed-import-modules': [
            'doctest', 'python_ta', 'random', 'typing',
            'block', 'goal', 'player', 'renderer',
            'pygame'
        ],
        'max-attributes': 10,
        'generated-members': 'pygame.*'
    })
