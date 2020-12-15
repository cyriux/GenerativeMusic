package com.martraire.chords;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class ChordTest {

	public static class Chord {

		public Chord(int... notes) {
			this.notes = notes;
		}

		private final int[] notes;

		@Override
		public int hashCode() {
			return Arrays.hashCode(notes);
		}

		@Override
		public boolean equals(Object other) {
			return Arrays.equals(notes, ((Chord) other).notes);
		}

		@Override
		public String toString() {
			return Arrays.toString(notes);
		}

	}

	public static enum ChromaticScaleNote {
		C, Db, D, Eb, E, F, Gb, G, Ab, A, Bb, B;
	}

	// Diatonic scales
	public static int[] MajorScale = { 0, 2, 4, 5, 7, 9, 11 };
	public static int[] MinorScale = { 0, 2, 3, 5, 7, 9, 10 };
	
	
	// Other scales not suited for forming chords
	public static int[] WholeToneScale = { 0, 2, 4, 6, 8, 10, 12 };

	public static int[] ChromaticScale = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

	public static int[] PentatonicScale = { 0, 2, 4, 7, 9 };
	// 1, 2, b3, 5, b6
	public static int[] HirajoshiScale = { 0, 2, 3, 7, 8 };
	// 1, b2, 4, 5, b6
	public static int[] SakuraScale = { 0, 1, 5, 7, 8 };
	// 1, b2, 4, 5, b7
	public static int[] InsenScale = { 0, 1, 5, 7, 10 };
	// 1, 2, 4, 5, 6
	public static int[] YoScale = { 0, 2, 5, 7, 9 };

	@Test
	public void testRootTriads() {
		assertEquals(new Chord(0, 4, 7), chord(MajorScale, 0));
		assertEquals(new Chord(0, 3, 7), chord(MinorScale, 0));
	}

	@Test
	public void testDegree2Triads() {
		assertEquals(new Chord(2, 5, 9), chord(MajorScale, 1));
		assertEquals(new Chord(2, 5, 9), chord(MinorScale, 1));
	}

	@Test
	public void testDegree5Triads() {
		assertEquals(new Chord(7, 11, 2), chord(MajorScale, 4));
		assertEquals(new Chord(7, 10, 2), chord(MinorScale, 4));
	}

	// I - ii - iii - IV - V - vi - vii
	// tonic I, iii, vi //
	// subdominant ii IV // can lead to any other chord
	// dominant V, vii // should resolve to tonic

	// in a diatonic scale
	static int[] tensionDegrees = { 1, 3, 6, 2, 6, 5, 7 };

	private Chord chord(int[] scale, int degree) {
		final int n = scale.length;
		final int note1 = scale[(0 + degree) % n];
		final int note2 = scale[(2 + degree) % n];
		final int note3 = scale[(4 + degree) % n];
		return new Chord(note1, note2, note3);
	}

}
