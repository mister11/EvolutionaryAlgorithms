package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TSPSolver {

	private static final double ALPHA = 1;
	private static final double BETA = 2;
	private static final double RO = 0.02;
	private static final int STAGNATION_THRESHOLD = 500;
	private static Random r = new Random();

	private List<City> cities;
	private CityDistance[][] distances;
	private double[][] heuristic;
	private List<Solution> solutions;
	private double[][] pheromones;
	private int size;
	private int maxIters;
	private Solution bestInIteration;
	private Solution globalBest;
	private int stagnationCounter;
	

	private double tMin;
	private double tMax;
	private double a;

	public TSPSolver(String filename, int k, int l, int maxIters) {
		this.maxIters = maxIters;
		this.cities = new Data(filename).getCities();
		this.solutions = new ArrayList<>();
		this.size = cities.size();
		this.stagnationCounter = 0;
		this.heuristic = new double[size][size];
		this.pheromones = new double[size][size];
		this.distances = new CityDistance[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				City first = cities.get(i);
				City second = cities.get(j);
				double xDist = first.getPoint().x - second.getPoint().x;
				double yDist = first.getPoint().y - second.getPoint().y;
				this.distances[i][j] = new CityDistance(first, second,
						Math.sqrt(xDist * xDist + yDist * yDist));
				this.heuristic[i][j] = Math.pow(
						(1 / this.distances[i][j].getDistance()), BETA);
			}
		}
		kNearestForCities(k);
		for (int i = 0; i < l; i++) {
			Solution s = new Solution();
			solutions.add(s);
		}
		run();
	}

	private void run() {
		int iter = 0;
		Solution init = getInitSolution();
		//tMax = 1 / (RO * init.tourLength);
		tMax = size / init.tourLength;
		a = (0.8 * (size - 1)) / 0.2;
		tMin = tMax / a;
		reinitializePheromones();
		while (iter++ < maxIters) {
			//System.out.println(iter);
			int size = solutions.size();
			for (int i = 0; i < size; i++) {
				//brisanje starih vrijednosti
				this.solutions.get(i).tour = new ArrayList<>();
				this.solutions.get(i).tourLength = 0;
				createTour(this.solutions.get(i));
			}
			findBest();
			System.out.println("Iteration: " + iter);
			System.out.println("Shortest path: " + globalBest.tourLength);
			evaporatePheromones();
			updatePheromonTrace(iter);
			if(isStagnation()) {
				reinitializePheromones();
				stagnationCounter = 0;
			}
		}
		System.out.println("\nProgram has ended! Here is final solution: ");
		System.out.println("Path: " + globalBest.tour);
		System.out.println("Length: " + globalBest.tourLength);
	}

	private Solution getInitSolution() {
		Solution s = new Solution();
		for(City c : cities) {
			s.tour.add(c);
		}
		evaluateSolution(s);
		return s;
	}

	private void evaluateSolution(Solution s) {
		int size = s.tour.size();
		for(int i=0; i < size - 1; i++) {
			City a = s.tour.get(i);
			City b = s.tour.get(i + 1);
			s.tourLength += this.distances[a.getId()][b.getId()].getDistance();
		}
		City first = s.tour.get(0);
		City last = s.tour.get(s.tour.size() - 1);
		s.tourLength += this.distances[last.getId()][first.getId()].getDistance();
	}

	private void reinitializePheromones() {
		for(int i=0; i < size; i++) {
			for(int j=0; j < size; j++) {
				this.pheromones[i][j] = tMax;
			}
		}
	}

	private boolean isStagnation() {
		stagnationCounter++;
		return stagnationCounter >= STAGNATION_THRESHOLD;
	}

	private void evaporatePheromones() {
		for(int i=0; i < size; i++) {
			for(int j=0; j < size; j++) {
				this.pheromones[i][j] *= (1 - RO);
				if(this.pheromones[i][j] < tMin) {
					this.pheromones[i][j] = tMin;
				}
			}
 		}
	}

	private void updatePheromonTrace(int iter) {
		Solution forUpdate;
		if(iter <= maxIters / 2) {
			forUpdate = bestInIteration;
		} else {
			forUpdate = globalBest;
		}
		int size = forUpdate.tour.size();
		double delta = 1 / forUpdate.tourLength;
		for(int i=0; i < size - 1; i++) {
			City a = forUpdate.tour.get(i);
			City b = forUpdate.tour.get(i + 1);
			this.pheromones[a.getId()][b.getId()] += delta;
			if(this.pheromones[a.getId()][b.getId()] > tMax) {
				this.pheromones[a.getId()][b.getId()] = tMax;
			}
			this.pheromones[b.getId()][a.getId()] += delta;
			if(this.pheromones[b.getId()][a.getId()] > tMax) {
				this.pheromones[b.getId()][a.getId()] = tMax;
			}
		}
		City first = forUpdate.tour.get(0);
		City last = forUpdate.tour.get(forUpdate.tour.size() - 1);
		this.pheromones[first.getId()][last.getId()] += delta;
		if(this.pheromones[first.getId()][last.getId()] > tMax) {
			this.pheromones[first.getId()][last.getId()] = tMax;
		}
		this.pheromones[last.getId()][first.getId()] += delta;
		if(this.pheromones[last.getId()][first.getId()] > tMax) {
			this.pheromones[last.getId()][first.getId()] = tMax;
		}
	}

	private void findBest() {
		bestInIteration = new Solution();
		bestInIteration.tour = new ArrayList<>(solutions.get(0).tour);
		bestInIteration.tourLength = solutions.get(0).tourLength;
		for(Solution s : solutions) {
			if(s.tourLength < bestInIteration.tourLength) {
				bestInIteration.tour = new ArrayList<>(s.tour);
				bestInIteration.tourLength = s.tourLength;
			}
		}
		if(globalBest == null) {
			globalBest = new Solution();
			globalBest.tour = new ArrayList<>(bestInIteration.tour);
			globalBest.tourLength = bestInIteration.tourLength;
		} else {
			if(bestInIteration.tourLength < globalBest.tourLength) {
				globalBest.tour = new ArrayList<>(bestInIteration.tour);
				globalBest.tourLength = bestInIteration.tourLength;
				tMax = 1 / (RO * globalBest.tourLength);
				tMin = tMax / a;
				stagnationCounter = 0;
			}
		}
		
	}

	private void createTour(Solution solution) {
		// add first random city to tour
		solution.tour.add(this.cities.get(r.nextInt(size)));
		for (int i = 1; i < this.size; i++) {	
			int cityId = solution.tour.get(i - 1).getId();
			List<City> nearest = solution.tour.get(i - 1).nearest;
			List<City> nearestAvailable = getAvailableCities(solution, nearest);
			if (nearestAvailable.isEmpty()) {
				nearestAvailable = getAllOtherCities(solution);
			}
			if (nearestAvailable.isEmpty()) {
				System.err.println("Break");
				break; // gotovi smo
			}

			int availableSize = nearestAvailable.size();
			double totalSumOfNearest = 0.0;
			double[] probabilities = new double[availableSize];
			for (int j = 0; j < availableSize; j++) {
				City neighbor = nearestAvailable.get(j);
				probabilities[j] = Math.pow(
						this.pheromones[cityId][neighbor.getId()], ALPHA)
						* this.heuristic[cityId][neighbor.getId()];
				totalSumOfNearest += probabilities[j];
			}
			for (int j = 0; j < availableSize; j++) {
				probabilities[j] = probabilities[j] / totalSumOfNearest;
			}
			City next = spinTheWheel(probabilities, nearestAvailable);
			solution.tour.add(next);
			solution.tourLength += this.distances[cityId][next.getId()]
					.getDistance();
		}
		City first = solution.tour.get(0);
		City last = solution.tour.get(solution.tour.size() - 1);
		solution.tourLength += this.distances[last.getId()][first.getId()]
				.getDistance();

	}

	private City spinTheWheel(double[] probabilities, List<City> nearest) {
		double threshold = r.nextDouble();
		double total = 0;
		int availableSize = nearest.size();
		for (int j = 0; j < availableSize; j++) {
			total += probabilities[j];
			if (threshold <= total) {
				return nearest.get(j);
			}
		}
		return nearest.get(availableSize - 1);
	}

	private List<City> getAllOtherCities(Solution solution) {
		List<City> others = new ArrayList<>();
		for (City c : cities) {
			if (!solution.tour.contains(c)) {
				others.add(c);
			}
		}
		return others;
	}

	private List<City> getAvailableCities(Solution solution, List<City> nearest) {
		List<City> available = new ArrayList<>();
		for (City c : nearest) {
			if (!solution.tour.contains(c)) {
				available.add(c);
			}
		}
		return available;
	}

	private void kNearestForCities(int k) {
		for (int i=0; i < size; i++) {
			CityDistance[] allDists = takeRow(i);
			Arrays.sort(allDists);
			City c = this.cities.get(i);
			c.nearest = new ArrayList<>();
			for (int j = 1; j <= k; j++) {
				c.nearest.add(allDists[j].getSecond());
			}
		}
	}

	private CityDistance[] takeRow(int row) {
		CityDistance[] allDist = new CityDistance[size];
		for(int i=0; i < size; i++) {
			allDist[i] = this.distances[row][i];
		}
		return allDist;
	}

	public static void main(String[] args) {
		if(args.length != 4) {
			System.err.println("Must have 4 parameters!");
			System.exit(-1);
		}
		new TSPSolver(args[0], Integer.parseInt(args[1]),
				Integer.parseInt(args[2]), Integer.parseInt(args[3]));
	}

}
