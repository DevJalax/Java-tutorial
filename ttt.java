import java.io.*;
import java.util.*;
int mv = 0;
Scanner s = new Scanner(System.in);
class ttt
{
	char board[][] = new char[3][3]; 
	char turn;
	int ch , row , col;


	public void disp_board()
	{
	 System.out.println("X is player 1 and O is player 2");
	 System.out.println("Enter player's turn");
         turn = s.next();
	 System.out.println("1"+"| "+"2"+" |"+"3");
	 System.out.println("4"+"|"+"5"+"|"+"6");
         System.out.println("7"+"|"+"8"+"|"+"9");
	}

	public void moves_inp()
	{
	 System.out.println("Enter the move number");
	 ch = s.nextInt();
	 switch(ch)
         {
	  case 1:
          row = 0;
	  col = 0;
	  break;
	  case 2:
	  row = 0;
	  col = 1;
	  break;
	  case 3:
	  row = 0;
	  col = 2;
	  break;
	  case 4:
	  row = 1;
	  col = 0;
	  break;
	  case 5:
	  row = 1;
	  col = 1;
	  break;
	  case 6:
	  row = 1;
	  col = 2;
	  break;
	  case 7:
	  row = 2;
	  col = 0;
	  break;
	  case 8:
	  row = 2;
	  col = 1;
	  break;
	  case 9:
	  row = 2;
	  col = 2;
	  break;
	  default :
	  break;  
	 }
	}
    
       	public void valid_move()
	{
		if(turn == 'X' || turn == 'O' && board[row][col] != 'X' && board[row][col] != 'O')
		{
			board[row][col] = turn;
		}
		else
		{
			System.out.print("Invalid move. please enter new move");
			moves_inp();
		}
	}

      int win_check()
      {
	      for(int i = 0; i<3; i++)
	      {
		      if(board[i][0] == turn && board[i][1] == turn && board[i][2] == turn || board[0][i] == turn && board[1][i] == turn && board[2][i] == turn || board[i][i] == turn || board[0][2] == turn && board[1][1] == turn && board[2][0] == turn)
		      {
			      return 1;
		      }
		      else
		      {
			      mv++;
		      }
	      }
      }	
}

public static void main(String args[])
{
	ttt p1 = new ttt();
	ttt p2 = new ttt();
	int w1,w2;
	System.out.println("    TICK TACK TOE   ");
	System.out.println("X player 1 and O player 2");
	p1.disp_board();
	p2.disp_board();
	if(p1.turn == p2.turn)
	{
		System.out.prinln("Turn already taken. Please change");
		p2.disp_board();
	}
	do
	{
		p1.moves_inp();
		p2.moves_inp();
		p1.valid_move();
		p2.valid_move();
		w1 = p1.win_check();
		w2 = p2.win_check();
	} while(w1 == 1 || w2 == 1 || mv ==9);

	if(w1 == 1)
	{
         System.out.println("Congrats winner is X or player 1");
	}
	else if(w2 == 1)
	{
		System.out.println("Congrats winner is O or player 2");
	}
	else if(mv == 9)
	{
		System.out.println("draw");
	}
}
