package com.openrsc.server.net.rsc.handlers;

import com.openrsc.server.external.GameObjectDef;
import com.openrsc.server.model.Point;
import com.openrsc.server.model.action.WalkToObjectAction;
import com.openrsc.server.model.entity.GameObject;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.net.Packet;
import com.openrsc.server.net.rsc.OpcodeIn;
import com.openrsc.server.net.rsc.PacketHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameObjectAction implements PacketHandler {
	/**
	 * The asynchronous logger.
	 */
	private static final Logger LOGGER = LogManager.getLogger();

	public void handlePacket(Packet packet, Player player) {
		int pID = packet.getID();

		if (player.inCombat()) {
			player.message("You can't do that whilst you are fighting");
			return;
		}
		if (player.isBusy()) {
			player.resetPath();
			return;
		}

		if (pID == OpcodeIn.OBJECT_COMMAND1.getOpcode()) {
			player.click = 0;
		}
		else if (pID == OpcodeIn.OBJECT_COMMAND2.getOpcode()) {
			player.click = 1;
		}
		else return;

		player.resetAll();

		final short x = packet.readShort();
		final short y = packet.readShort();
		if (x < 0 || y < 0) {
			player.setSuspiciousPlayer(true, "bad game object coordinates");
			return;
		}

		final GameObject object = player.getViewArea().getGameObject(Point.location(x, y));
		if (object == null) {
			player.setSuspiciousPlayer(true, "game object action null object");
			return;
		}

		player.setWalkToAction(new WalkToObjectAction(player, object) {
			public void executeInternal() {
				getPlayer().resetPath();
				GameObjectDef def = object.getGameObjectDef();
				if (getPlayer().isBusy() || !getPlayer().atObject(object) || getPlayer().isRanging() || def == null) {
					return;
				}

				getPlayer().resetAll();
				String command = (getPlayer().click == 0 ? def.getCommand1() : def
					.getCommand2()).toLowerCase();

				int playerDirection = getPlayer().getSprite();
				if (getPlayer().getWorld().getServer().getPluginHandler().handlePlugin(
					getPlayer(),
					"OpLoc",
					new Object[]{getPlayer(), object, command},
					this)) {
					getPlayer().setSprite(playerDirection);
				}
			}
		});
	}
}
