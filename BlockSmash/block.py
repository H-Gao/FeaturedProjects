"""Assignment 2 - Blocky

=== CSC148 Fall 2017 ===
Diane Horton and David Liu
Department of Computer Science,
University of Toronto


=== Module Description ===

This file contains the Block class, the main data structure used in the game.
"""
from typing import Optional, Tuple, List
import random
import math
from renderer import COLOUR_LIST, TEMPTING_TURQUOISE, BLACK, colour_name


HIGHLIGHT_COLOUR = TEMPTING_TURQUOISE
FRAME_COLOUR = BLACK


class Block:
    """A square block in the Blocky game.

    === Public Attributes ===
    position:
        The (x, y) coordinates of the upper left corner of this Block.
        Note that (0, 0) is the top left corner of the window.
    size:
        The height and width of this Block.  Since all blocks are square,
        we needn't represent height and width separately.
    colour:
        If this block is not subdivided, <colour> stores its colour.
        Otherwise, <colour> is None and this block's sublocks store their
        individual colours.
    level:
        The level of this block within the overall block structure.
        The outermost block, corresponding to the root of the tree,
        is at level zero.  If a block is at level i, its children are at
        level i+1.
    max_depth:
        The deepest level allowed in the overall block structure.
    highlighted:
        True iff the user has selected this block for action.
    children:
        The blocks into which this block is subdivided.  The children are
        stored in this order: upper-right child, upper-left child,
        lower-left child, lower-right child.
    parent:
        The block that this block is directly within.

    === Representation Invariations ===
    - len(children) == 0 or len(children) == 4
    - If this Block has children,
        - their max_depth is the same as that of this Block,
        - their size is half that of this Block,
        - their level is one greater than that of this Block,
        - their position is determined by the position and size of this Block,
          as defined in the Assignment 2 handout, and
        - this Block's colour is None
    - If this Block has no children,
        - its colour is not None
    - level <= max_depth
    """
    position: Tuple[int, int]
    size: int
    colour: Optional[Tuple[int, int, int]]
    level: int
    max_depth: int
    highlighted: bool
    children: List['Block']
    parent: Optional['Block']

    def __init__(self, level: int,
                 colour: Optional[Tuple[int, int, int]] = None,
                 children: Optional[List['Block']] = None) -> None:
        """Initialize this Block to be an unhighlighted root block with
        no parent.

        If <children> is None, give this block no children.  Otherwise
        give it the provided children.  Use the provided level and colour,
        and set everything else (x and y coordinates, size,
        and max_depth) to 0.  (All attributes can be updated later, as
        appropriate.)
        """
        # Initialize the x, y coordinates, size, max_depth to zero and
        # highlighted as false.
        self.position = (0, 0)
        self.size = 0
        self.max_depth = 0
        self.highlighted = False

        # Initialize the colour and level to the provided arguments.
        self.colour = colour
        self.level = level

        # To meat to type contract, if the children argument is None -
        # this blocks children is to an EMPTY LIST not None.
        if children is None:
            self.children = []
        else:
            self.children = children

        self.parent = None

    def rectangles_to_draw(self) -> List[Tuple[Tuple[int, int, int],
                                               Tuple[int, int],
                                               Tuple[int, int],
                                               int]]:
        """
        Return a list of tuples describing all of the rectangles to be drawn
        in order to render this Block.

        This includes (1) for every undivided Block:
            - one rectangle in the Block's colour
            - one rectangle in the FRAME_COLOUR to frame it at the same
              dimensions, but with a specified thickness of 3
        and (2) one additional rectangle to frame this Block in the
        HIGHLIGHT_COLOUR at a thickness of 5 if this block has been
        selected for action, that is, if its highlighted attribute is True.

        The rectangles are in the format required by method Renderer.draw.
        Each tuple contains:
        - the colour of the rectangle
        - the (x, y) coordinates of the top left corner of the rectangle
        - the (height, width) of the rectangle, which for our Blocky game
          will always be the same
        - an int indicating how to render this rectangle. If 0 is specified
          the rectangle will be filled with its colour. If > 0 is specified,
          the rectangle will not be filled, but instead will be outlined in
          the FRAME_COLOUR, and the value will determine the thickness of
          the outline.

        The order of the rectangles does not matter.
        """
        # The list of rectangles to be drawn
        rect_lst = []

        # A tuple of this block's length/width (stored to prevent repetition)
        size_tuple = (self.size, self.size)

        if len(self.children) == 4:
            # If the block is subdivided, draw it's sub-blocks
            # Subdivided blocks have no borders/colours.
            for child in self.children:
                rect_lst.extend(child.rectangles_to_draw())
        else:
            # If the block is a single unit (not subdivided), then draw its
            # frame and filled colour.
            rect_lst.append((self.colour, self.position, size_tuple, 0))
            rect_lst.append((FRAME_COLOUR, self.position, size_tuple, 3))

        if self.highlighted:
            # If the block is highlighted, draw the highlighted frame.
            # Regardless of if its subdivided or not.
            rect_lst.append((HIGHLIGHT_COLOUR, self.position, size_tuple, 5))

        # Returns the list of rectangles
        return rect_lst

    def swap(self, direction: int) -> None:
        """Swap the child Blocks of this Block.

        If <direction> is 1, swap vertically.  If <direction> is 0, swap
        horizontally. If this Block has no children, do nothing.
        """
        # Checks if the block is subdivided - does nothing otherwise.
        if len(self.children) == 4:
            if direction == 1:
                # The block is subdivided and is swapping vertically.

                # Swaps the children's positions so:
                # 1. Top-right child = Bottom-right child (and vice versa)
                # 2. Top-left child = Bottom-left child (and vice versa)
                self.children[0], self.children[3] \
                    = self.children[3], self.children[0]

                self.children[1], self.children[2] \
                    = self.children[2], self.children[1]
            else: # direction == 0
                # The block is subdivided and is swapping horizontally.

                # Swaps the children's positions so:
                # 1. Top-right child = Top-left child (and vice versa)
                # 2. Bottom-right child = Bottom-left child (and vice versa)
                self.children[0], self.children[1] \
                    = self.children[1], self.children[0]

                self.children[2], self.children[3] \
                    = self.children[3], self.children[2]

            # Updates the locations so the children appears at the new positions
            self.update_block_locations(self.position, self.size)

    def rotate(self, direction: int) -> None:
        """Rotate this Block and all its descendants.

        If <direction> is 1, rotate clockwise.  If <direction> is 3, rotate
        counterclockwise. If this Block has no children, do nothing.
        """
        # Checks if the block is subdivided - does nothing otherwise.
        if len(self.children) == 4:
            if direction == 1:
                # The block is subdivided and is rotating clockwise.

                # Swaps the child blocks positions so...
                # Top-right child => Bottom-right child => Bottom-left child
                # => Top-left child => Top-right child.
                self.children[0], self.children[1], \
                    self.children[2], self.children[3] = \
                    self.children[1], self.children[2],\
                    self.children[3], self.children[0]
            else: # direction == 3
                # The block is subdivided and is rotating counter-clockwise.

                # Swaps the child blocks positions so..
                # Top-right child => Top-left child => Bottom-left child
                # => Bottom-right child => Top-right child
                self.children[0], self.children[1], \
                    self.children[2], self.children[3] = \
                    self.children[3], self.children[0], \
                    self.children[1], self.children[2]

            # Rotate each of this block's children as well
            for child in self.children:
                child.rotate(direction)

        # Update the descendants positions, so they appear at the new positions
        self.update_block_locations(self.position, self.size)

    def smash(self) -> bool:
        """Smash this block.

        If this Block can be smashed,
        randomly generating four new child Blocks for it.  (If it already
        had child Blocks, discard them.)
        Ensure that the RI's of the Blocks remain satisfied.

        A Block can be smashed iff it is not the top-level Block and it
        is not already at the level of the maximum depth.

        Return True if this Block was smashed and False otherwise.
        """
        # Makes sure this block is not the top or bottom-most levels.
        if 0 < self.level < self.max_depth:
            # Stores a list of the newly generated children
            children_lst = []

            for _ in range(4):
                # Generates 4 random children blocks and stores them
                child = random_init(self.level + 1, self.max_depth)
                child.parent = self # Rand_init does not do this automatically

                children_lst.append(child)

            # Sets this block's children as the generated children, and update
            # their locations and return True (smashed successfully)
            self.children = children_lst
            self.update_block_locations(self.position, self.size)
            return True

        else:
            # This block is the top or bottom most level, so return that it
            # was NOT smashed successfully.
            return False

    def update_block_locations(self, top_left: Tuple[int, int],
                               size: int) -> None:
        """
        Update the position and size of each of the Blocks within this Block.

        Ensure that each is consistent with the position and size of its
        parent Block.

        <top_left> is the (x, y) coordinates of the top left corner of
        this Block.  <size> is the height and width of this Block.
        """
        # Sets this blocks location to the provided position and size
        self.position = top_left
        self.size = size

        # Stores this current blocks x and y position.
        curr_x = top_left[0]
        curr_y = top_left[1]

        # Stores half of the size (rounded to the nearest int). This is because
        # the position/size type contracts specifies an INTEGER not a double.
        half_size = round(size / 2.0)

        for i in range(len(self.children)):
            # Stores the <i>ith child of this block.
            child = self.children[i]

            # Stores the child's blocks position of its top-left corner.
            child_top_left = None # None is only a "default" value

            if i == 0: # Child is the top-right block
                # Its correct position is (PARENT.X + SIZE/2, PARENT.Y)
                child_top_left = (curr_x + half_size, curr_y)

            elif i == 1: # Child is the top-left block
                # Its correct position is (PARENT.X, PARENT.Y)
                child_top_left = (curr_x, curr_y)

            elif i == 2:  # Child is the bottom-left block
                # Its correct position is (PARENT.X, PARENT.Y + SIZE/2)
                child_top_left = (curr_x, curr_y + half_size)

            else: # i == 3, Child is the bottom-right block
                # Its correct position is (PARENT.X + SIZE/2, PARENT.Y + SIZE/2)
                child_top_left = (curr_x + half_size, curr_y + half_size)

            # new_top_left now stores the (x, y) of the child's top-left corner
            # Update the children's blocks with the correct position and size
            child.update_block_locations(child_top_left, half_size)

    def get_selected_block(self, location: Tuple[int, int], level: int) \
            -> 'Block':
        """Return the Block within this Block that includes the given location
        and is at the given level. If the level specified is lower than
        the lowest block at the specified location, then return the block
        at the location with the closest level value.

        <location> is the (x, y) coordinates of the location on the window
        whose corresponding block is to be returned.
        <level> is the level of the desired Block.  Note that
        if a Block includes the location (x, y), and that Block is subdivided,
        then one of its four children will contain the location (x, y) also;
        this is why <level> is needed.

        Preconditions:
        - 0 <= level <= max_depth
        """
        # Stores the x, y positions from the location
        x = self.position[0]
        y = self.position[1]

        # If this block is the selected block (according to the arguments)
        is_desired_block \
            = self.level == level and x < location[0] < x + self.size \
            and y < location[1] < y + self.size

        # If this is the selected block or cannot be subdivided, then this
        # block must be the selected one. Return it.
        if len(self.children) == 0 or is_desired_block:
            return self

        # Otherwise, this block is not the selected block - so the selected
        # block must be one of its descendants... We will search them now.
        else:
            # If the specified position is in the left half of this block...
            if location[0] < x + self.size/2:

                # The specified position is in also the top half. So, search
                # this block's top-left child.
                if location[1] < y + self.size/2:
                    return self.children[1].get_selected_block(location, level)

                # The specified position is also in the bottom half. So, search
                # this block's bottom-left child.
                else: # location[1] >= y + self.size/2
                    return self.children[2].get_selected_block(location, level)

            # If the specified position is in the right half of this block...
            else: # location[0] >= x + self.size/2

                # The specified position is also in the top half. So, search
                # this block's top-right child.
                if location[1] < y + self.size/2:
                    return self.children[0].get_selected_block(location, level)

                # The specified position is also in the bottom half. So, search
                # this block's bottom-right child.
                else: # location[1] >= y + self.size/2
                    return self.children[3].get_selected_block(location, level)

    def flatten(self) -> List[List[Tuple[int, int, int]]]:
        """Return a two-dimensional list representing this Block as rows
        and columns of unit cells.

        Return a list of lists L, where, for 0 <= i, j < 2^{self.max_depth -
        self.level}
            - L[i] represents column i and
            - L[i][j] represents the unit cell at column i and row j.
        Each unit cell is represented by 3 ints for the colour
        of the block at the cell location[i][j]

        L[0][0] represents the unit cell in the upper left corner of the Block.
        """
        # Stores the size of the current level IN TERMS OF THE SMALLEST BLOCK.
        # Ie. 1 unit_size = 1 smallest block, 2 unit_size = 2 smallest blocks.
        unit_size = pow(2, self.max_depth - self.level)

        # If this block is a base (coloured) block, then create a
        # (unit_size x unit_size) 2D array and will it with this block's colour
        if len(self.children) == 0:
            # Stores this block's 2D array representation
            block_rep = []

            # Fills the 2D array with this block colour
            for row in range(unit_size):
                # Appends an empty row
                block_rep.append([])

                # This loop fills the empty row from the last line.
                for _ in range(unit_size):
                    block_rep[row].append(self.colour)

            # Return the filled 2D array.
            return block_rep

        # If this block is subdivided, fill it with its children 2D array
        # representations.
        else: # len(self.children) == 4
            # Returns the 2D array of this block.
            # This is in a private method to:
            # 1. Avoid code cluster.
            # 2. Avoid a PyTA error.
            return self._fill_subdivided_block(unit_size)

    def _fill_subdivided_block(self, unit_size: int):
        """
        self is a subdivided block. Fill and return a 2D array that is filled
        with self's sub-block's 2D array representations, in their assigned
        positions. Ex. child 0's representation in the the top-right
        quadrant, child 1's representation is in the top-left quadrant, etc.

        Precondition: self refers to a subdivided block
        Ie. len(self.children) == 4.
        """
        # Stores the 2D representation of its 4 children.
        top_right = self.children[0].flatten()
        top_left = self.children[1].flatten()
        bottom_left = self.children[2].flatten()
        bottom_right = self.children[3].flatten()

        # Stores the CURRENT blocks 2D representation
        block_rep = []

        # This nested loop fills the 2D array...
        # 1. Each row from top to bottom.
        # 2. WITHIN EACH row, it goes left to right.
        for row in range(unit_size):
            # Appends an empty column (creates a starting point for the rows)
            block_rep.append([])

            # Stores the mid-point of the 2D array.
            half_size = unit_size // 2

            for col in range(unit_size):
                # Now we have a (col, row) position (from the loop index)...
                # We detect which quadrant (top-right, top_left,
                # bottom_right or bottom_left) that (col, row) is in
                # Then, we fill the quadrant of THIS blocks 2D array with the
                # children's 2D array.
                # Ex. Fill the top right quadrant /w top right children 2D array
                if col < half_size <= row:
                    block_rep[row].append(top_right[row - half_size][col])

                elif row < half_size and col < half_size:
                    block_rep[row].append(top_left[row][col])

                elif col >= half_size > row:
                    block_rep[row].append(bottom_left[row][col - half_size])

                else:  # x >= unit_size/2 and y >= unit_size/2
                    block_rep[row].append(
                        bottom_right[row - half_size][col - half_size])

        # Returns the 2D array of this block.
        return block_rep


def random_init(level: int, max_depth: int) -> 'Block':
    """Return a randomly-generated Block with level <level> and subdivided
    to a maximum depth of <max_depth>.

    Throughout the generated Block, set appropriate values for all attributes
    except position and size.  They can be set by the client, using method
    update_block_locations.

    Precondition:
        level <= max_depth
    """
    # Creates and stores a randomly generated block (with the provided level,
    # provided max_depth, no children and no color)
    block = Block(level, None, None)
    block.max_depth = max_depth

    # Stores a random value (from 0.0 to 1.0)
    rand_val = random.random()

    # If this block CAN create sub-blocks (ie. not the max depth) and it
    # randomly decides to create sub-blocks (by the formula)
    if level < max_depth and rand_val < math.exp(-0.25 * level):
        for _ in range(4):
            # Create 4 random children and stores them.
            block.children.append(random_init(level+1, max_depth))

        for child in block.children:
            # Sets this CURRENT block as it's children's parent.
            child.parent = block
    else:
        # Sets this block's colour to a random colour from the colour list.
        colour_id = random.randint(0, len(COLOUR_LIST)-1)
        block.colour = COLOUR_LIST[colour_id]

    # Returns the generated block.
    return block


def attributes_str(b: Block, verbose) -> str:
    """Return a str that is a concise representation of the attributes of <b>.

    Include attributes position, size, and level.  If <verbose> is True,
    also include highlighted, and max_depth.

    Note: These are attributes that every Block has.
    """
    answer = f'pos={b.position}, size={b.size}, level={b.level}, '
    if verbose:
        answer += f'highlighted={b.highlighted}, max_depth={b.max_depth}'
    return answer


def print_block(b: Block, verbose=False) -> None:
    """Print a text representation of Block <b>.

    Include attributes position, size, and level.  If <verbose> is True,
    also include highlighted, and max_depth.

    Precondition: b is not None.
    """
    print_block_indented(b, 0, verbose)


def print_block_indented(b: Block, indent: int, verbose) -> None:
    """Print a text representation of Block <b>, indented <indent> steps.

    Include attributes position, size, and level.  If <verbose> is True,
    also include highlighted, and max_depth.

    Precondition: b is not None.
    """
    if len(b.children) == 0:
        # b a leaf.  Print its colour and other attributes
        print(f'{"  " * indent}{colour_name(b.colour)}: ' +
              f'{attributes_str(b, verbose)}')
    else:
        # b is not a leaf, so it doesn't have a colour.  Print its
        # other attributes.  Then print its children.
        print(f'{"  " * indent}{attributes_str(b, verbose)}')
        for child in b.children:
            print_block_indented(child, indent + 1, verbose)


if __name__ == '__main__':
    import python_ta
    python_ta.check_all(config={
        'allowed-io': ['print_block_indented'],
        'allowed-import-modules': [
            'doctest', 'python_ta', 'random', 'typing',
            'block', 'goal', 'player', 'renderer', 'math'
        ],
        'max-attributes': 15
    })

    # This tiny tree with one node will have no children, highlighted False,
    # and will have the provided values for level and colour; the initializer
    # sets all else (position, size, and max_depth) to 0.
    b0 = Block(0, COLOUR_LIST[2])
    # Now we update position and size throughout the tree.
    b0.update_block_locations((0, 0), 750)
    print("=== tiny tree ===")
    # We have not set max_depth to anything meaningful, so it still has the
    # value given by the initializer (0 and False).
    print_block(b0, True)

    b1 = Block(0, children=[
        Block(1, children=[
            Block(2, COLOUR_LIST[3]),
            Block(2, COLOUR_LIST[2]),
            Block(2, COLOUR_LIST[0]),
            Block(2, COLOUR_LIST[0])
        ]),
        Block(1, COLOUR_LIST[2]),
        Block(1, children=[
            Block(2, COLOUR_LIST[1]),
            Block(2, COLOUR_LIST[1]),
            Block(2, COLOUR_LIST[2]),
            Block(2, COLOUR_LIST[0])
        ]),
        Block(1, children=[
            Block(2, COLOUR_LIST[0]),
            Block(2, COLOUR_LIST[2]),
            Block(2, COLOUR_LIST[3]),
            Block(2, COLOUR_LIST[1])
        ])
    ])
    b1.update_block_locations((0, 0), 750)
    print("\n=== handmade tree ===")
    # Similarly, max_depth is still 0 in this tree.  This violates the
    # representation invariants of the class, so we shouldn't use such a
    # tree in our real code, but we can use it to see what print_block
    # does with a slightly bigger tree.
    print_block(b1, True)

    # Now let's make a random tree.
    # random_init has the job of setting all attributes except position and
    # size, so this time max_depth is set throughout the tree to the provided
    # value (3 in this case).
    b2 = random_init(0, 3)
    # Now we update position and size throughout the tree.
    b2.update_block_locations((0, 0), 750)
    print("\n=== random tree ===")
    # All attributes should have sensible values when we print this tree.
    print_block(b2, True)
