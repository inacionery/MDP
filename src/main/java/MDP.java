public class MDP {
	private static int _columns = 4;
	private static int _rows = 3;

	private static int _maxInteractions = 1000;

	private static double _discountValue = 1;
	private static double _probabilityBad = 0.1;
	private static double _probabilityGood = 0.8;
	private static double _reward = -0.4;

	private static double _matrix[][] = new double[_rows][_columns];
	private static String _policyMatrix[][] = new String[_rows][_columns];
	private static double _rewardMatrix[][] = new double[_rows][_columns];

	public static void main(String[] args) {
		initMatrix();
		initRewardMatrix();

		int n = 0;
		printMatrix(n);
		while (n < _maxInteractions) {
			updateMatrix();

			n++;

			printMatrix(n);
		}

		printPolicy();
	}

	private static double aDown(int row, int column) {
		if (row == (_rows - 1)) {
			return _matrix[row][column];
		}
		return _matrix[row + 1][column];
	}

	private static double aLeft(int row, int column) {
		if (column == 0) {
			return _matrix[row][column];
		}
		return _matrix[row][column - 1];
	}

	private static double aRight(int row, int column) {
		if (column == (_columns - 1)) {
			return _matrix[row][column];
		}
		return _matrix[row][column + 1];
	}

	private static double aUp(int row, int column) {
		if (row == 0) {
			return _matrix[row][column];
		}
		return _matrix[row - 1][column];
	}

	private static int bestAction(double matrix[]) {
		int best = 0;

		for (int i = 1; i < matrix.length; i++) {
			if (matrix[i] > matrix[best]) {
				best = i;
			}
		}
		return best;
	}

	private static void initMatrix() {
		for (int r = 0; r < _rows; r++) {
			for (int c = 0; c < _columns; c++) {
				_matrix[r][c] = 0;
			}
		}
	}

	private static void initRewardMatrix() {
		for (int r = 0; r < _rows; r++) {
			for (int c = 0; c < _columns; c++) {
				_rewardMatrix[r][c] = _reward;
			}
		}

		_rewardMatrix[0][3] = 1;
		_rewardMatrix[1][1] = -0.5;
		_rewardMatrix[1][3] = -1;
		_rewardMatrix[2][3] = 0.2;
	}

	private static void printMatrix(int n) {
		System.out.println("After " + n + " iterations:");
		System.out.println();
		for (int r = 0; r < _rows; r++) {
			for (int c = 0; c < _columns; c++) {
				System.out.printf("% 6.4f\t", _matrix[r][c]);
			}
			System.out.println();
		}
		System.out.println();
	}

	private static void printPolicy() {
		_policyMatrix[0][3] = "+";
		_policyMatrix[1][3] = "-";

		System.out.println("Best policy:");
		System.out.println();
		for (int r = 0; r < _rows; r++) {
			for (int c = 0; c < _columns; c++) {
				System.out.print(_policyMatrix[r][c] + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}

	private static void updateMatrix() {
		double auxMatrix[][] = new double[_rows][_columns];

		for (int row = 0; row < _rows; row++) {
			for (int column = 0; column < _columns; column++) {

				double actionMatrix[] = new double[4];

				if (((row == 0) && (column == 3))
						|| ((row == 1) && (column == 3))) {
					auxMatrix[row][column] = _rewardMatrix[row][column];
				}
				else {
					actionMatrix[0] = (aUp(row, column) * _probabilityGood)
							+ (aLeft(row, column) * _probabilityBad)
							+ (aRight(row, column) * _probabilityBad);
					actionMatrix[1] = (aDown(row, column) * _probabilityGood)
							+ (aLeft(row, column) * _probabilityBad)
							+ (aRight(row, column) * _probabilityBad);
					actionMatrix[2] = (aRight(row, column) * _probabilityGood)
							+ (aDown(row, column) * _probabilityBad)
							+ (aUp(row, column) * _probabilityBad);
					actionMatrix[3] = (aLeft(row, column) * _probabilityGood)
							+ (aDown(row, column) * _probabilityBad)
							+ (aUp(row, column) * _probabilityBad);

					int best = bestAction(actionMatrix);

					auxMatrix[row][column] = _rewardMatrix[row][column]
							+ (_discountValue * actionMatrix[best]);

					_policyMatrix[row][column] = (best == 0 ? "UP"
							: (best == 1 ? "DW" : (best == 2 ? "RG" : "LF")));
				}
			}
		}

		for (int r = 0; r < _rows; r++) {
			for (int c = 0; c < _columns; c++) {
				_matrix[r][c] = auxMatrix[r][c];
			}
		}
	}
}