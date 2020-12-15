package com.martraire.trigger;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tap all micro-timing things
 * 
 * - tap-reset: reset the beats, ie. update absolute time offset to now
 * (per-track, or global?) - tap-tempo: input the tempo from the average elapsed
 * time between two successive taps - tap-swing: input the swing ratio as the
 * proportion between short/long elapsed time between the last three taps -
 * tap-off-kilter: input the microtiming from the gap between the absolute tap
 * time vs. the closest pulse time - tap-euclidian (number of onsets during the
 * length)
 * 
 * - tap-overdub, tap-mute - random (random mute) - groove profile?
 * 
 * 
 * Euclidian all the rhythm pattern things
 * 
 * - stretch special case of changing the number of onsets - clock divider
 * (scaling), special case of euclidian with multiple onset/pulses - arbitrary
 * length on top of euclidian pattern (truncate or add padding) - rotation: a
 * phase within the euclidian pattern - fill: a special case of reducing pulse
 * 
 **/
public class SequencerTest {

	@Test
	public void swing() {
		// t+ = (50 + swing) * t / 50
		// t- = (50 - swing) * t / 50
		// where t is the base loop period without swing
		assertEquals("x", euclidianString(1, 1, 0, 1));
		assertEquals("x.", euclidianString(1, 2, 0, 2));
	}

	@Test
	public void euclidianRhythm1() {
		assertEquals("x", euclidianString(1, 1, 0, 1));
		assertEquals("x.", euclidianString(1, 2, 0, 2));
	}

	@Test
	public void euclidianRhythm2() {
		assertEquals("x.", euclidianString(1, 2, 0, 2));
		assertEquals("xx", euclidianString(2, 2, 0, 2));
	}

	@Test
	public void euclidianRhythm3() {
		assertEquals("x..", euclidianString(1, 3, 0, 3));
		assertEquals("x.x", euclidianString(2, 3, 0, 3));
		assertEquals("xxx", euclidianString(3, 3, 0, 3));
	}

	@Test
	public void euclidianRhythm4() {
		assertEquals("x...", euclidianString(1, 4, 0, 4));
		assertEquals("x.x.", euclidianString(2, 4, 0, 4));
		assertEquals("x.xx", euclidianString(3, 4, 0, 4));
	}

	@Test
	public void euclidianRhythm8() {
		assertEquals("x..x..x.", euclidianString(3, 8, 0, 8));
		assertEquals("x.x.xx.x", euclidianString(5, 8, 0, 8));
	}

	@Test
	public void euclidianRhythm13_24() {
		assertEquals("x.x.x.x.x.x.xx.x.x.x.x.x", euclidianString(13, 24, 0, 24));
	}

	@Test
	public void euclidianRhythmRotation() {
		assertEquals("x...", euclidianStringRotation(1, 4, 0));
		assertEquals("...x", euclidianStringRotation(1, 4, 1));
		assertEquals("..x.", euclidianStringRotation(1, 4, 2));
		assertEquals(".x..", euclidianStringRotation(1, 4, 3));
		assertEquals("x...", euclidianStringRotation(1, 4, 4));

		assertEquals("x.x.xx.x", euclidianStringRotation(5, 8, 0));
		assertEquals(".x.xx.xx", euclidianStringRotation(5, 8, 1));

		assertEquals("xx.x.xx.", euclidianStringRotation(5, 8, 7));

		assertEquals("xx.xx.x.", euclidianStringRotation(5, 8, 4));
		assertEquals("xx.xx.x.", euclidianStringRotation(5, 8, 12));

		assertEquals("x.xx.xx.", euclidianStringRotation(5, 8, 2)); // paper
		assertEquals("x.xx.x.x.x.x.xx.x.x.x.x.", euclidianStringRotation(13, 24, 10)); // paper
	}

	@Test
	public void euclidianRhythmLength() {
		assertEquals("x.x.xx.x", euclidianString(5, 8, 0, 8));
		assertEquals("x.x.xx.x", euclidianStringLength(5, 8, 8));
		assertEquals("x.x.", euclidianStringLength(5, 8, 4));
		assertEquals("x.x.xx.x....", euclidianStringLength(5, 8, 12));
	}

	@Test
	public void euclidianRhythmFull() {
		assertEquals("x.x.xx.x", euclidianString(5, 8, 0, 8));
		assertEquals("x.x.xx.x", euclidianStringLength(5, 8, 8));
		assertEquals("x.x.", euclidianStringLength(5, 8, 4));
		assertEquals("....x.x.xx.x", euclidianString(5, 8, 8, 12));
	}

	private final String euclidianStringRotation(int onsets, int pulses, int phase) {
		return euclidianString(onsets, pulses, phase, pulses);
	}

	private final String euclidianStringLength(int onsets, int pulses, int length) {
		return euclidianString(onsets, pulses, 0, length);

	}

	private final String euclidianString(int onsets, int pulses, int phase, int length) {
		String s = "";
		for (int i = 0; i < length; i++) {
			s += euclidian(onsets, pulses, phase, length, i) ? "x" : ".";
		}
		return s;
	}

	// ----

	/**
	 * Calculates whether there's a beat on the current cursor
	 * 
	 * @param onsets The number of beats per cycle
	 * @param pulses The number of pulses per cycle
	 * @param phase The rotation phase (modulo the length)
	 * @param length The actual length of the pattern, may be shorter or longer than the pulses
	 * @param cursor The current cursor (time)
	 * @return true when there's a beat, false otherwise
	 */
	public final boolean euclidian(int onsets, int pulses, int phase, int length, int cursor) {
		int i = (cursor + phase) % length;
		return i < pulses && isBeat(onsets, pulses, i);
	}

	private final boolean isBeat(int onsets, int pulses, int i) {
		int value = bresenham(i, onsets, pulses);
		int previous = i == 0 ? -1 : bresenham(i - 1, onsets, pulses);
		return value != previous;
	}

	private final int bresenham(int index, int onsets, int pulses) {
		return (index * onsets) / pulses;
	}

}
