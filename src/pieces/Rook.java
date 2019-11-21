package pieces;

import board.Board;
import board.Square;
import util.Move;

public class Rook extends Piece {

    public Rook(Color color) {
        super(color);
        this.type = Type.ROOK;
    }

    @Override
    public boolean validateMove(Move move) {
        // executeMove or capture
        if ((move.getCapturedPiece() == null)
                || (move.getCapturedPiece() != null
                    && !move.getPiece().getColor().equals(move.getCapturedPiece().getColor()))) {
            // along file
            if (move.getDestinationFile() == move.getOriginFile()
                    && move.getDestinationRank() != move.getOriginRank()) {
                return true;
            }
            // along rank
            if (move.getDestinationFile() != move.getOriginFile()
                    && move.getDestinationRank() == move.getOriginRank()) {
                return true;
            }
        }

        // all other cases
        return false;
    }
    @Override
    public boolean specialMove(Move move) {
        return true;
    }
    public void setDangered(Square square){
        //dangeredSquares.clear();
        dangeredSquares.add(square);
    }
    public void getDangered(){
        for (Square square: dangeredSquares) {
            System.out.println(square);
        }
    }
    /*public void dangered(){
        int a = getRank()+1;
        while (a<=8){
                Square currentSquare = Board.getSquare(getFile(), a);
                setDangered(currentSquare);
                if (currentSquare.getCurrentPiece() != null) {
                    break;
                }
                a++;
            getDangered();
            }
        }
*/
}
