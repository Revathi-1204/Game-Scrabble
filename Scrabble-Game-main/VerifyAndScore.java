
package com.boardui;

import java.util.ArrayList;
import java.util.Stack;


import javafx.scene.control.Button;


public class VerifyAndScore 
{
	Player player;
	Board board;
	Dictionary dict;
	
	Stack<Tile> recentlyPlayedTileStack = new Stack<>();
	Stack<Cell> recentlyPlayedCellStack = new Stack<>(); 
	Stack<Button> recentlyPlayedCellButtonStack = new Stack<>();
	ArrayList<Cell> occupiedCells = new ArrayList<Cell>(); 
	
	Tile rackTileSelected;  
	boolean initialMove = true;
	
	public VerifyAndScore(Player player, Board board)
	{
		this.player = player;
		
		this.board = board;
		
		dict = new Dictionary();
		
		rackTileSelected = null;
		player.setScore(0);
		
	}
	
	public boolean checkBoard()
	{
		if(initialMove == true && recentlyPlayedCellStack.size() == 1)
		{
			return false;
		}
		
		if(recentlyPlayedCellStack.size () == 0)
		{
			return false;
		}
		if(board.cellMatrix[7][7].getTile() == null)
		{
			return false;
		}
		if(isConnectedHorizontally() == false && isConnectedVertically() == false)
		{
			return false;
		}
		if(isConnectedToPast() == false)
		{
			return false;
		}
		if(verifyHorizontalWords() == false || verifyVerticalWords() == false)
		{
			return false;
		}
		if(isConnectedHorizontally() == true && isConnectedVertically() == false)
		{
			Cell leftMost = getLeftMost(recentlyPlayedCellStack.peek());
			player.addScore(scoreHorizontalWord(leftMost) + scoreVToHMove());
		}
		else if(isConnectedVertically() == true && isConnectedHorizontally() == false)
		{
			Cell topMost = getTopMost(recentlyPlayedCellStack.peek());
			player.addScore(scoreVerticalWord(topMost) + scoreHToVMove());
		}
		else if(isConnectedVertically() == true && isConnectedHorizontally() == true)
		{
			Cell leftMost = getLeftMost(recentlyPlayedCellStack.peek());
			Cell topMost = getTopMost(recentlyPlayedCellStack.peek());
			player.addScore(scoreHorizontalWord(leftMost) + scoreVerticalWord(topMost));
		}
		
		occupiedWords();
		clearStacks();
		initialMove = false;
		return true;
	}
	
	public int scoreHToVMove()
	{
		Cell[] cellArray = recentWord();
		int score = 0;
		
		for(int i = 0; i < cellArray.length; i++)
		{
			Cell leftMost = getLeftMost(cellArray[i]);
			score = score + scoreHorizontalWord(leftMost);
		}
		return score;
	}
	
	public int scoreVToHMove()
	{
		Cell[] cellArray = recentWord();
		int score = 0;
		
		for(int i = 0; i < cellArray.length; i++)
		{
			Cell topMost = getTopMost(cellArray[i]);	
			score = score + scoreVerticalWord(topMost);
		}
		return score;
	}
	
	public int scoreHorizontalWord(Cell leftMost)
	{
		Cell current = leftMost;
		int wordMultiply = 1;
		int wordScore = 0;
		boolean hasOneTile = true; 
		
		while(current.getTile() != null)
		{
			wordMultiply = wordMultiply + getWordBonus(current);
			wordScore = wordScore + (current.getTile().getPoints() * getLetterBonus(current));
		
			if(current.getRight() != null)
			{
				if(current.getRight().getTile() != null)
				{
					current = current.getRight();
					hasOneTile = false;
				}
				else
				{
					break;
				}
			}
			else
			{
				break;
			}
		}
		
		if(hasOneTile == true)
		{
			return 0;
		}
		
		return wordScore * wordMultiply;
	}
	
	public int scoreVerticalWord(Cell topMost)
	{
		Cell current = topMost;
		int wordMultiply = 1;
		int wordScore = 0;
		boolean hasOneTile = true;
		
		while(current.getTile() != null)
		{
			wordMultiply = wordMultiply + getWordBonus(current);
			wordScore = wordScore + (current.getTile().getPoints() * getLetterBonus(current));
		
			if(current.getBottom() != null)
			{
				if(current.getBottom().getTile() != null)
				{
					current = current.getBottom();
					hasOneTile = false;
				}
				else
				{
					break;
				}
			}
			else
			{
				break;
			}
		}
		
		if(hasOneTile == true)
		{
			return 0;
		}
		
		return wordScore * wordMultiply;
	}
	
	public int getWordBonus(Cell cell)
	{
		if(occupiedCells.contains(cell))
		{
			return 0;
		}
		else if(cell.getBonus() == "TW")
		{
			return 3;
		}
		else if(cell.getBonus() == "DW")
		{
			return 2;
		}
		
		return 0;
	}
	
	public int getLetterBonus(Cell cell)
	{
		if(occupiedCells.contains(cell))
		{
			return 1;
		}
		else if(cell.getBonus() == "TL")
		{
			return 3;
		}
		else if(cell.getBonus() == "DL")
		{
			return 2;
		}
		
		return 1;
	}
	
	public void clearStacks()
	{
		recentlyPlayedTileStack.clear();
		recentlyPlayedCellButtonStack.clear();
	}
	
	public void occupiedWords()
	{
		while(!recentlyPlayedCellStack.isEmpty())
		{
			occupiedCells.add(recentlyPlayedCellStack.pop());
		}
	}
	
	public Cell[] recentWord()
	{
		Cell[] cellArray = new Cell[recentlyPlayedCellStack.size()]; 
		recentlyPlayedCellStack.toArray(cellArray);
		
		return cellArray;
	}
	
	public boolean isConnectedToPast()
	{
		if(occupiedCells.size() == 0)
		{
			return true;
		}
		
		Cell[] recentWord = convertToArray();
	
		for(Cell currentCell : recentWord)
		{
			if(traverseLeftMost(currentCell) == true || traverseRightMost(currentCell) == true || traverseUpMost(currentCell) == true || traverseDownMost(currentCell) == true )
			{
				return true;
			}
		}
		
		return false;
	}
	
	public Cell getLeftMost(Cell cell)
	{
		Cell current = cell;
		
		while(current.getLeft() != null)
		{
			if(current.getLeft().getTile() != null)
			{
				current = current.getLeft();
			}
			else
			{
				break;
			}
		}
		
		return current;
	}
	
	public Cell getTopMost(Cell cell)
	{
		Cell current = cell;
		
		while(current.getTop() != null)
		{
			if(current.getTop().getTile() != null)
			{
				current = current.getTop();
			}
			else
			{
				break;
			}
		}
		
		return current;
	}
	
	public boolean traverseLeftMost(Cell cell)
	{
		Cell current = cell;
		
		while(current.getLeft() != null)
		{
			if(current.getLeft().getTile() != null)
			{
				current = current.getLeft();
				
				if(occupiedCells.contains(current))
				{
					return true;
				}
			}
			else
			{
				break;
			}
		}
		return false;
	}
	
	public boolean traverseRightMost(Cell cell)
	{
		Cell current = cell;
		
		while(current.getRight() != null)
		{
			if(current.getRight().getTile() != null)
			{
				current = current.getRight();
				
				if(occupiedCells.contains(current))
				{
					return true;
				}
			}
			else
			{
				break;
			}
		}
		return false;
	}
	
	public boolean traverseUpMost(Cell cell)
	{
		Cell current = cell;
		
		while(current.getTop() != null)
		{
			if(current.getTop().getTile() != null)
			{
				current = current.getTop();
				
				if(occupiedCells.contains(current))
				{
					return true;
				}
			}
			else
			{
				break;
			}
		}
		return false;
	}
	
	public boolean traverseDownMost(Cell cell)
	{
		Cell current = cell;
		
		while(current.getBottom() != null)
		{
			if(current.getBottom().getTile() != null)
			{
				current = current.getBottom();
				
				if(occupiedCells.contains(current))
				{
					return true;
				}
			}
			else
			{
				break;
			}
		}
		return false;
	}
	
	public boolean isConnectedHorizontally()
	{
		Cell currentCell = recentlyPlayedCellStack.peek();
		
		while(currentCell.getLeft() != null)
		{
			if(currentCell.getLeft().getTile() != null)
			{
				currentCell = currentCell.getLeft();
			}
			else
			{
				break;
			}
		}
		
		Cell[] recentWord = convertToArray();
		int count = 0;
		
		if(isCellRecentlyPlayed(currentCell, recentWord) == true)
		{
			count++;
		}
		
		while(currentCell.getRight() != null)
		{
			if(currentCell.getRight().getTile() != null)
			{
				currentCell = currentCell.getRight();
				
				if(isCellRecentlyPlayed(currentCell, recentWord) == true)
				{
					count++;
				}
			}
			else
			{
				break;
			}
		}
		
		if(count == recentWord.length)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isConnectedVertically()
	{
		Cell currentCell = recentlyPlayedCellStack.peek();
		
		while(currentCell.getTop() != null)
		{
			if(currentCell.getTop().getTile() != null)
			{
				currentCell = currentCell.getTop();
			}
			else
			{
				break;
			}
		}
		
		Cell[] recentWord = convertToArray();
		int count = 0;
		
		if(isCellRecentlyPlayed(currentCell, recentWord) == true)
		{
			count++;
		}
		
		while(currentCell.getBottom() != null)
		{
			if(currentCell.getBottom().getTile() != null)
			{
				currentCell = currentCell.getBottom();
				
				if(isCellRecentlyPlayed(currentCell, recentWord) == true)
				{
					count++;
				}
			}
			else
			{
				break;
			}
		}
		
		if(count == recentWord.length)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean verifyHorizontalWords()
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < 15; i++)
		{
			for(int j = 0; j < 15; j++)
			{
				Cell currentCell = board.cellMatrix[i][j];
				
				if(currentCell.getTile() != null)
				{
					sb.append(currentCell.getTile().getLetter());
				}
				else if((currentCell.getTile() == null && sb.length() > 1) || (j == 14 && sb.length() > 1))
				{
					String word = sb.toString();
					
					if(dict.verifyWord(word) == false)
					{
						System.out.println("Failed: " + word);
						sb.setLength(0);
						return false;
					}
					else
					{
						System.out.println("Passed: " + word);
						sb.setLength(0);
					}
				}
				else if((currentCell.getTile() == null && sb.length() == 1) || (j == 14 && sb.length() == 1))
				{
					sb.setLength(0);
				}
			}
			
			sb.setLength(0);
		}
		return true;
	}
	
	public boolean verifyVerticalWords()
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < 15; i++)
		{
			for(int j = 0; j < 15; j++)
			{
				Cell currentCell = board.cellMatrix[j][i];
				
				if(currentCell.getTile()!= null)
				{
					sb.append(currentCell.getTile().getLetter());
				}
				else if((currentCell.getTile() == null && sb.length() > 1) || (j == 14 && sb.length() > 1))
				{
					String word = sb.toString();
					
					if(dict.verifyWord(word) == false)
					{
						System.out.println("Failed: " + word);
						sb.setLength(0);
						return false;
					}
					else
					{
						System.out.println("Passed: " + word);
						sb.setLength(0);
					}
				}
				else if((currentCell.getTile() == null && sb.length() == 1) || (j == 14 && sb.length() == 1))
				{
					sb.setLength(0);
				}
			}
			
			sb.setLength(0);
		}
		return true;
	}
	
	public Cell[] convertToArray()
	{
		int size = recentlyPlayedCellStack.size();
		Cell[] cellStack = new Cell[size];
		recentlyPlayedCellStack.toArray(cellStack);
		return cellStack;
	}
	
	public boolean isCellRecentlyPlayed(Cell currentCell, Cell[] cellArray)
	{
		if(currentCell == null)
		{
			return false;
		}
		
		for(Cell i : cellArray)
		{
			if(i == currentCell)
			{
				return true;
			}
		}
		
		return false;
	}
}
	
	
