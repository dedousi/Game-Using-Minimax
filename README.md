# Just a game with MINIMAX for an AI class (UOI).
Create a two-player game program where players take turns on an MxN board. Each player has a piece occupying a square on the board, with Player MAX's piece starting in the middle of the top row and Player MIN's piece in the middle of the bottom row.
<br>
The black squares represent visited or crossed squares and cannot be traversed again. Players move their pieces like a chess queen, but only on white squares and cannot occupy or cross the square of the opponent's piece. When a piece moves, the starting square and any crossed squares turn black.
<br>
A player loses if they have no valid moves during their turn. MAX plays first and uses the MINIMAX algorithm to decide on moves, implemented recursively.
<br>
The program should define starting positions for MAX and MIN, as well as optional initial black squares. Clearly state any assumptions made regarding valid moves.
