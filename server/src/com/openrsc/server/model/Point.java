package com.openrsc.server.model;

import com.openrsc.server.model.entity.WildernessLocation;
import com.openrsc.server.model.entity.WildernessLocation.WildState;
import com.openrsc.server.model.world.Area;
import com.openrsc.server.util.rsc.Formulae;

import java.util.ArrayList;

public class Point {

	private static ArrayList<WildernessLocation> wildernessLocations = new ArrayList<WildernessLocation>();

	static {
		/* Edgeville dungeon wilderness, always members wild */
		wildernessLocations.add(new WildernessLocation(WildState.MEMBERS_WILD, 195, 3206, 234, 3258));
		/* Red Dragons, always P2P */
		wildernessLocations.add(new WildernessLocation(WildState.MEMBERS_WILD, 129, 180, 163, 219));
		/* Underground Lava maze, always P2P */
		wildernessLocations.add(new WildernessLocation(WildState.MEMBERS_WILD, 243, 2988, 283, 3020));
	}

	protected short x, y;

	protected Point() {
	}

	public Point(int x, int y) {
		this((short)x, (short)y);
	}

	public Point(short x, short y) {
		this.x = x;
		this.y = y;
	}

	public static Point location(int x, int y) {
		return location((short)x, (short)y);
	}
	public static Point location(short x, short y) {
		if (x < 0 || y < 0) {
			throw new IllegalArgumentException(
				"Point may not contain non negative values x:" + x + " y:"
					+ y);
		}
		return new Point(x, y);
	}
	
	public String returnLocationName() {
		if (inHeroQuestRangeRoom()) {
			return "Hero's Quest Range Room";
		}

		else if (inTutorialLanding()) {
			return "Tutorial Landing";
		}

		else if (aroundTutorialRatZone()) {
			return "Tutorial Rats";
		}

		else if (onTutorialIsland()) {
			return "Tutorial Island";
		}

		else if (onBlackHole()) {
			return "Black Hole";
		}

		else if (inModRoom()) {
			return "Mod Room";
		}

		else if (inFreeWild()) {
			return "F2P Wilderness";
		}

		else if (inWilderness()) {
			return "Wilderness";
		}

		else if (inVarrock()) {
			return "Varrock";
		}

		else if (inEdgeville()) {
			return "Edgeville";
		}

		else if (inBarbVillage()) {
			return "Barbarian Village";
		}

		else if (inDraynor()) {
			return "Draynor";
		}

		else if (inLumbridge()) {
			return "Lumbridge";
		}

		else if (inAlKharid()) {
			return "Al Kharid";
		}

		else if (inFalador()) {
			return "Falador";
		}

		else if (inPortSarim()) {
			return "Port Sarim";
		}

		else if (inTaverly()) {
			return "Taverly";
		}

		else if (inEntrana()) {
			return "Entrana";
		}

		else if (inCatherby()) {
			return "Catherby";
		}

		else if (isInSeersPartyHall()) {
			return "Seers Party Hall";
		}

		else if (inSeers()) {
			return "Seers";
		}

		else if (inGnomeStronghold()) {
			return "Gnome Stronghold";
		}

		else if (inArdougne()) {
			return "Ardougne";
		}

		else if (inYanille()) {
			return "Yanille";
		}

		else if (inBrimhaven()) {
			return "Brimhaven";
		}

		else if (inShiloVillage()) {
			return "Shilo Village";
		}

		else if (inKaramja()) {
			return "Karamja";
		}

		else if (isInFisherKingRealm()) {
			return "Fisher King Realm";
		}

		else if (isInsideGrandTreeGround()) {
			return "Grand Tree";
		}

		return getX() + "," + getY();
	}

	public static boolean inWilderness(int x, int y) {
		int wild = 2203 - (y + (1776 - (944 * (int) (y / 944))));
		if (x + 2304 >= 2640) {
			wild = -50;
		}
		if (wild > 0) {
			return (1 + wild / 6) >= 1;
		}
		return false;
	}

	public boolean isMembersWild() {
		if (inWilderness()) {
			for (WildernessLocation location : wildernessLocations) {
				if (x >= location.getMinX() && y >= location.getMinY() && x <= location.getMaxX() && y <= location.getMaxY()) {
					if (location.getWildState() == WildState.MEMBERS_WILD) {
						return true;
					} else if (location.getWildState() == WildState.FREE_WILD) {
						return false;
					}
				}
			}
			/* If its allowed in these wild levels */
			return wildernessLevel() >= 48 && wildernessLevel() <= 56;
			/* It is F2P */
		}
		/* Not in wild, its P2P */
		return true;
	}

	public WildernessLocation getWildernessLocation() {
		for (WildernessLocation location : wildernessLocations) {
			if (x > location.getMinX() && y > location.getMinY() && x < location.getMaxX() && y < location.getMaxY()) {
				return location;
			}
		}
		return null;
	}

	public final boolean withinRange(Point point, int radius) {
		/*int xDiff = this.x - p.x;
		int yDiff = this.y - p.y;

		return xDiff <= radius && xDiff >= -radius && yDiff <= radius
			&& yDiff >= -radius;*/
		return getDistanceTo(point) <= radius;
	}

	public final boolean withinGridRange(Point point, int gridSize) {
		// Snap coordinates to an 8x8 grid
		// radius is compared in multiples of 8
		final int xDiff = (this.x >> 3) - (point.x >> 3);
		final int yDiff = (this.y >> 3) - (point.y >> 3);
		return xDiff <= gridSize && xDiff >= -gridSize && yDiff <= gridSize && yDiff >= -gridSize;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public int hashCode() {
		return (x << 16) | y;
	}

	public boolean inBounds(int x1, int y1, int x2, int y2) {
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}

	public boolean inHeroQuestRangeRoom() {
		return inBounds(459, 672, 460, 673);
	}

	public boolean onTutorialIsland() {
		return inBounds(190, 720, 240, 770);
	}

	public boolean onBlackHole() {
		return inBounds(303, 3298, 307, 3302);
	}

	public boolean inTutorialLanding() {
		return inBounds(214, 739, 221, 747);
	}

	public boolean aroundTutorialRatZone() {
		return inBounds(226, 728, 234, 738);
	}

	public boolean inModRoom() {
		return inBounds(64, 1639, 80, 1643);
	}

	public boolean inWilderness() {
		return wildernessLevel() > 0;
	}

	public boolean inFreeWild() {
		return (wildernessLevel() >= 1 && wildernessLevel() <= 48);
	}

	public boolean inVarrock() {
		return inBounds(78, 490, 175, 537) || inBounds(92, 444, 150, 490);
	}

	public boolean inEdgeville() {
		return inBounds(198, 427, 229, 450) || inBounds(208, 451, 227, 472);
	}

	public boolean inBarbVillage() {
		return inBounds(209, 491, 247, 529);
	}

	public boolean inDraynor() {
		return inBounds(210, 608, 233, 659);
	}

	public boolean inLumbridge() {
		return inBounds(108, 620, 147, 670);
	}

	public boolean inAlKharid() {
		return inBounds(48, 659, 96, 703);
	}

	public boolean inFalador() {
		return inBounds(245, 531, 341, 583);
	}

	public boolean inPortSarim() {
		return inBounds(246, 621, 286, 670);
	}

	public boolean inTaverly() {
		return inBounds(343, 454, 389, 512);
	}

	public boolean inEntrana() {
		return inBounds(395, 525, 441, 573);
	}

	public boolean inCatherby() {
		return inBounds(415, 475, 456, 508);
	}

	public boolean inSeers() {
		return inBounds(486, 438, 534, 482);
	}

	public boolean inGnomeStronghold() {
		return inBounds(673, 432, 751, 537);
	}

	public boolean inArdougne() {
		return inBounds(500, 537, 600, 708);
	}

	public boolean inYanille() {
		return inBounds(577, 741, 647, 767);
	}

	public boolean inBrimhaven() {
		return inBounds(435, 644, 477, 709);
	}

	public boolean inKaramja() {
		return inBounds(323, 644, 679, 908);
	}

	public boolean inShiloVillage() {
		return inBounds(384, 817, 430, 860);
	}

	public boolean isInSeersPartyHall() {
		return isInSeersPartyHallUpstairs() || isInSeersPartyHallDownstairs();
	}

	public boolean isInSeersPartyHallUpstairs() {
		return inBounds(490, 1408, 500, 1415);
	}

	public boolean isInSeersPartyHallDownstairs() {
		return inBounds(490, 464, 500, 471);
	}

	public boolean isInFisherKingRealm() {
		return inBounds(388, 4, 427, 40) || inBounds(484, 4, 523, 40)
				|| inBounds(411, 976, 519, 984)
				|| inBounds(411, 1920, 518, 1925)
				|| inBounds(511, 976, 519, 984)
				|| inBounds(511, 1920, 518, 1925);
	}

	public boolean isInsideGrandTreeGround() {
		return inBounds(410, 158, 422, 170);
	}

	public int wildernessLevel() {
		int wild = 2203 - (y + (1776 - (944 * Formulae.getHeight(this))));
		if (x + 2304 >= 2640) {
			wild = -50;
		}
		if (wild > 0) {
			return 1 + wild / 6;
		}
		return 0;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Point) || o == null) {
			return false;
		}

		Point point = (Point) o;
		return x == point.x && y == point.y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public int getDistanceTo(Point o2) {
		/*int xDiff = Math.abs(getX() - o2.getX());
		int yDiff = Math.abs(getY() - o2.getY());
		return xDiff + yDiff;*/
		return (int)Math.sqrt(Math.pow(getX() - o2.getX(), 2) + Math.pow(getY() - o2.getY(), 2));
	}

	public boolean inDwarfArea() {
		return inBounds(240, 432, 309, 527);
	}

	public boolean inPlatformArea() {
		return inBounds(492, 614, 498, 620);
	}

	public boolean inMageArena() {
		return inBounds(220, 122, 236, 137);
	}

	public boolean inTouristTrapCave() {
		return inBounds(49, 3600, 95, 3647);
	}

	public boolean inTouristTrapCave1() {
		return inBounds(79, 3614, 95, 3647);
	}

	public boolean inTouristTrapCave2() {
		return inBounds(48, 3633, 78, 3647);
	}

	public boolean inTouristTrapCave3() {
		return inBounds(49, 3600, 95, 3647);
	}

	public boolean isInsideFlameWall() {
		return inBounds(450, 3704, 455, 3711);
	}

	public boolean isAroundBoulderRock() {
		return inBounds(404, 3730, 418, 3744)
				|| inBounds(407, 3718, 421, 3732)
				|| inBounds(417, 3716, 431, 3730);
	}

	public boolean isAroundTotemPole() {
		return inBounds(360, 881, 374, 895)
				|| inBounds(388, 889, 402, 903)
				|| inBounds(456, 882, 470, 896);
	}

	public boolean inArea(Area area) {
		return area.inBounds(this);
	}
}
