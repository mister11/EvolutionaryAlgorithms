package hr.fer.zemris.optjava.dz2;


public class Matrix {

	private double[][] elements;
	private int rows;
	private int cols;

	
	public Matrix(final int rows, final int cols) {
		if (rows < 0 || cols < 0) {
			throw new IllegalArgumentException(
					"Rows and cols argumetns must be greater than 0.");
		}
		this.rows = rows;
		this.cols = cols;
		elements = new double[rows][cols];
	}


	public Matrix(final int rows, final int cols, final double[][] array,
			final boolean useGiven) {
		if (rows < 1 || cols < 1) {
			throw new IllegalArgumentException(
					"Rows and cols argumetns must be greater than 0.");
		}
		if (useGiven) {
			elements = array;
		} else {
			elements = new double[rows][cols];
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					elements[i][j] = array[i][j];
				}
			}
		}
		this.rows = rows;
		this.cols = cols;
	}

	
	public int getRowsCount() {
		return rows;
	}

	public int getColsCount() {
		return cols;
	}

	public double get(int row, int col) {
		if (rows < 0 || cols < 0) {
			throw new IllegalArgumentException(
					"Rows and cols argumetns must be greater or equal to 0.");
		}
		return elements[row][col];
	}


	public Matrix set(int row, int col, double value) {
		if (rows < 0 || cols < 0) {
			throw new IllegalArgumentException(
					"Rows and cols argumetns must be greater or equal to 0.");
		}
		elements[row][col] = value;
		return this;
	}


	public Matrix copy() {
		return new Matrix(rows, cols, elements, false);
	}

	
	public Matrix newInstance(int rows, int cols) {
		return new Matrix(rows, cols);
	}

	
	public static Matrix parseSimple(String input) {
		String input1 = input.trim().replace("|", " | ").replaceAll(" +", " ")
				.replaceAll("  ", " ");
		String[] separateRows = getRows(input1);

		// test if all rows have same number of elements
		int numOfColumns = getNumberOfElements(separateRows[0]);
		int numOfRows = separateRows.length;
		double[][] newDoubleArray = new double[numOfRows][numOfColumns];
		for (int i = 0; i < numOfRows; i++) {
			if (numOfColumns != getNumberOfElements(separateRows[i])) {
				throw new IllegalArgumentException(
						"All rows must contain same nuber of elements");
			}
			String[] helpArray = separateRows[i].trim().replaceAll(" +", " ")
					.split(" ");
			for (int j = 0; j < numOfColumns; j++) {
				newDoubleArray[i][j] = Double.parseDouble(helpArray[j]);
			}
		}

		return new Matrix(numOfRows, numOfColumns, newDoubleArray, false);
	}

	
	private static int numberOfRows(String input) {
		int numberOfRows = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '|') {
				numberOfRows++;
			}
		}
		return numberOfRows + 1;
	}


	private static String[] getRows(String input1) {
		String[] rows = new String[numberOfRows(input1)];
		StringBuilder builder = new StringBuilder();
		int j = 0;
		for (int i = 0; i < input1.length(); i++) {
			if (i != input1.length() && input1.charAt(i) != '|') {
				builder.append(input1.charAt(i));
			} else {
				rows[j] = builder.toString().trim();
				builder.setLength(0);
				j++;
			}
		}
		rows[j] = builder.toString().trim();

		return rows;
	}


	private static int getNumberOfElements(String input) {
		int count = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == ' ') {
				count++;
			}
		}
		return count + 1;
	}

}