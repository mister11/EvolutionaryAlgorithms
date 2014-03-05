package hr.fer.zemris.optjava.dz4.part2;

public class Function implements IFunction {
	
	private int maxHeight;
	
	public Function(int maxHeight) {
		this.maxHeight = maxHeight;
	}
	
	@Override
	public int valueAt(int[] v) {
		int usedBoxes = 1;
		int size = v.length;
		int currentSize = 0;
		for(int i=0; i < size; i++) {
			if(currentSize + v[i] <= this.maxHeight) {
				currentSize += v[i];
			} else {
				usedBoxes++;
				currentSize = v[i];
			}
		}
		return usedBoxes;
	}

}
