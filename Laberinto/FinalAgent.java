package nsgl.agents.examples.labyrinth.teseo.simple;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @author  Ivan Solano 
 **/
public class FinalAgent extends SimpleTeseoAgentProgram {
	
	public static String [][] map; // VISITED / CLOSEPATH / DIVERGENCE / NOTVISITED /GOAL
	public static String action ="ADVANCE"; // ADVANCE / GETBACK
	public static int orientation;
	public static int x,y;
	public static ArrayList <Integer> moves;
	public static Stack <movement>historicMoves;
	public static int goal = 0;
	
	public FinalAgent() {
		map = new String [200][200];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				map[i][j]="NOTVISITED";
			}
		}
		x = 99;
		y = 99;
		orientation = 0;
		moves = new ArrayList <Integer>();
		historicMoves = new Stack <movement>();
	}

	@Override
	public int accion(boolean PF, boolean PD, boolean PA, boolean PI, boolean MT, boolean FAIL) {
		// TODO Auto-generated method stub
		if(MT) {
			goal += 1;
			if(goal==1) {
				map[x][y]= "GOAL";
				printMap();
				map = new String [200][200];
				for (int i = 0; i < map.length; i++) {
					for (int j = 0; j < map.length; j++) {
						map[i][j]="NOTVISITED";
					}
				}
				x = 99;
				y = 99;
				orientation = 0;
				moves = new ArrayList <Integer>();
				historicMoves = new Stack <movement>();
			}	
			return -1;
		}else {
			goal = 0;
			return choose(PF, PD, PA, PI);
		}
	}

	public static int choose(boolean PF, boolean PD, boolean PA, boolean PI) {
		
		map[x][y]= "VISITED"; //marco donde estoy
		
		printMap();
		
		candidateMove(PF,PD,PA,PI); //cargo los posibles movimientos de los lugares que no he visitado
		System.out.print("moves: ");
		for (int i = 0; i < moves.size(); i++) {
			System.out.print(moves.get(i));
		}
		System.out.println();
		System.out.println("Action: "+action);
		int mov = move(); // decido que movimiento realizar y lo retorno
		System.out.println("movimiento selec: "+mov);
		
		run(mov); // ejecuto el movimiento
		System.out.println("nuevas x: "+x+" y:"+y);
		System.out.print("historicmoves: ");
		for (int i = 0; i < historicMoves.size(); i++) {
			System.out.print(historicMoves.get(i).mov);
		}
		System.out.println();
		System.out.println("actual orientacion: "+orientation);
		orientation = newOrientation(mov); // cargo mi nueva orientacion dado el movimiento que decidi
		System.out.println("nueva orientacion: "+orientation);
		
		return mov; // retorno el movimiento
	}
	
	public static void candidateMove(boolean PF, boolean PD, boolean PA, boolean PI) {
		
		moves= new ArrayList<Integer>();
		
		String FRENTE="",DERECHA="",IZQUIERDA="",ATRAS="";
		switch (orientation) {
			case 0://mirando al norte
				FRENTE=map[x-1][y]; // estado de la casilla de al frente 
				DERECHA=map[x][y+1]; // estado de la casilla de a la derecha
				IZQUIERDA=map[x][y-1]; //estado de la casilla de a la izquierda
				ATRAS=map[x+1][y]; //estado de la casilla de atras
				break;
			case 1://mirando al oriente
				FRENTE=map[x][y+1]; // estado de la casilla de al frente
				DERECHA=map[x+1][y]; // estado de la casilla de a la derecha
				IZQUIERDA=map[x-1][y]; //estado de la casilla de a la izquierda
				ATRAS=map[x][y-1]; //estado de la casilla de atras
				break;
			case 2: //mirando al sur
				FRENTE=map[x+1][y]; //estado de la casilla de al frente
				DERECHA=map[x][y-1]; //estado de la casilla de a la derecha
				IZQUIERDA=map[x][y+1];//estado de la casilla de a la izquierda
				ATRAS=map[x-1][y];// estado de la casilla de atras
				break;
			case 3: //mirando al occidente
				FRENTE=map[x][y-1];// estado de la casilla de al frente
				DERECHA=map[x-1][y]; // estado de la casilla de a la derecha
				IZQUIERDA=map[x+1][y];// estado de la casilla de a la izquierda
				ATRAS=map[x][y+1];// estado de la casilla de atras
				break;
		}
		
		if(!PF && "NOTVISITED".equals(FRENTE)) {
			moves.add(0);
		}if(!PA && "NOTVISITED".equals(ATRAS)) {
			moves.add(2);
		}if(!PI && "NOTVISITED".equals(IZQUIERDA)) {
			moves.add(3);
		}if(!PD && "NOTVISITED".equals(DERECHA)) {
			moves.add(1);
		}
		
	}
	
	public static int move() {

		if(moves.size()>1) { //si es mayor a 1 es porque hay mas de una vifurcacion aca por tanto deberia en algun momento volver aca, si es necesario
			map [x][y]="DIVERGENCE";
			action= "ADVANCE";
			historicMoves.push(new movement(moves.get(0),orientation,x,y)); // añado el movimiento al historico
			return moves.get(0);
		}else {
			if(moves.size()==1) { // si es exactamente 1 es porque puedo seguir avanzando pero solo por un camino
				map[x][y] ="VISITED";
				action= "ADVANCE";
				historicMoves.push(new movement(moves.get(0),orientation,x,y)); // añado el movimiento al historico
				return moves.get(0);
			}else{
				map [x][y] = "CLOSEPATH"; // si no es porque no hay caminos que no haya visitado por tanto es un camino cerrado para mi, debo devolverme
				
				int mov = 0;
				switch (action) {
				case "ADVANCE": //si estaba avanzando debo devolverme cambiar de estado
					mov = getBack();
					action= "GETBACK";
					break;
				case "GETBACK": // si me estaba devolviendo debo continuar haciendolo
					mov = getBack();
					break;
				}	
				return mov;
			}
		}
	}
	
	public static int getBack() {
		
		movement last = historicMoves.pop();
		
		int mov = 0;
		
		int xx = x -last.x;
		int yy = y -last.y;
		
		switch (orientation) {
		case 0:
				//movimiento vertical
				switch (xx) {
				case 1:
					mov = 0;
					break;
				case -1:
					mov = 2;
					break;
				}
				//movimiento horizontal
				switch (yy) {
				case 1:
					mov = 3;
					break;
				case -1:
					mov = 1;
					break;
				}
			break;
		case 1:
			//movimiento vertical
			switch (xx) {
			case 1:
				mov = 3;
				break;
			case -1:
				mov = 1;
				break;
			}
			//movimiento horizontal
			switch (yy) {
			case 1:
				mov = 2;
				break;
			case -1:
				mov = 0;
				break;
			}
			break;
		case 2:
			//movimiento vertical
			switch (xx) {
			case 1:
				mov = 2;
				break;
			case -1:
				mov = 0;
				break;
			}
			//movimiento horizontal
			switch (yy) {
			case 1:
				mov = 1;
				break;
			case -1:
				mov = 3;
				break;
			}
			break;
		case 3:
			//movimiento vertical
			switch (xx) {
			case 1:
				mov = 1;
				break;
			case -1:
				mov = 3;
				break;
			}
			//movimiento horizontal
			switch (yy) {
			case 1:
				mov = 0;
				break;
			case -1:
				mov = 2;
				break;
			}
			break;
		}
		return mov;

	}
	
	public static void run(int move) {
		switch (orientation) {
			case 0:
				switch (move) {
					case 0:
						x--;
						break;
					case 1:
						y++;
						break;
					case 2:
						x++;
						break;
					case 3:
						y--;
						break;
				}
				break;
			case 1:
				switch (move) {
					case 0:
						y++;
						break;
					case 1:
						x++;
						break;
					case 2:
						y--;
						break;
					case 3:
						x--;
						break;
				}
				
				break;
			case 2:
				switch (move) {
					case 0:
						x++;
						break;
					case 1:
						y--;
						break;
					case 2:
						x--;
						break;
					case 3:
						y++;
						break;
				}
				
				break;
			case 3:
					switch (move) {
					case 0:
						y--;
						break;
					case 1:
						x--;
						break;
					case 2:
						y++;
						break;
					case 3:
						x++;
						break;
				}
				
				break;
		}
	}
	
	public static int newOrientation(int move) {
		int newOrientation = 0;
		switch (orientation) {
		case 0: //mirando al norte
			switch (move) {
				case 0: // moverme al frente
					newOrientation = 0;
					break;
				case 2: // moverme atras
					newOrientation = 2;
					break;
				case 3: // moverme a la izquierda
					newOrientation = 3;
					break;
				case 1: // moverme a la derecha
					newOrientation = 1;
					break;
			}
			break;
		case 2: //mirando al sur
			switch (move) {
				case 0: // moverme al frente
					newOrientation = 2;
					break;
				case 2: // moverme atras
					newOrientation = 0;
					break;
				case 3: // moverme a la izquierda
					newOrientation = 1;
					break;
				case 1: // moverme a la derecha
					newOrientation = 3;
					break;
			}
			break;
		case 3: //mirando al occidente
			switch (move) {
				case 0: // moverme al frente
					newOrientation = 3;
					break;
				case 2: // moverme atras
					newOrientation = 1;
					break;
				case 3: // moverme a la izquierda
					newOrientation = 2;
					break;
				case 1: // moverme a la derecha
					newOrientation = 0;
					break;
			}
			break;
		case 1: //mirando al oriente
			switch (move) {
				case 0: // moverme al frente
					newOrientation = 1;
					break;
				case 2: // moverme atras
					newOrientation = 3;
					break;
				case 3: // moverme a la izquierda
					newOrientation = 0;
					break;
				case 1: // moverme a la derecha
					newOrientation = 2;
					break;
			}
			break;
		}
		return newOrientation;
	}
	
	public static boolean orientationInvert(int orientation1 , int orientation2) {
		
		if(orientation1+2%4 == orientation2 || orientation2+2%4 == orientation1) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public static void printMap() {
		for (int i = 99; i < 119; i++) {
			for (int j = 99; j < 119; j++) {
				if(map[i][j].equals("NOTVISITED")) {
					System.out.print(" |NV|");
				}if(map[i][j].equals("VISITED")) {
					System.out.print(" |VI|");
				}if(map[i][j].equals("CLOSEPATH")) {
					System.out.print(" |CP|");
				}if(map[i][j].equals("DIVERGENCE")) {
					System.out.print(" |DV|");
				}if(map[i][j].equals("GOAL")) {
					System.out.print(" |GG|");
				}	
			}
			System.out.println();
		}
		System.out.println("***************************************************************************************************");
	}
}
