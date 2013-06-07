/*
 * This file is part of MyPet
 *
 * Copyright (C) 2011-2013 Keyle
 * MyPet is licensed under the GNU Lesser General Public License.
 *
 * MyPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.util.support;

import com.garbagemule.MobArena.MobArenaHandler;
import com.garbagemule.MobArena.events.ArenaPlayerJoinEvent;
import de.Keyle.MyPet.entity.types.MyPet.PetState;
import de.Keyle.MyPet.util.MyPetBukkitUtil;
import de.Keyle.MyPet.util.MyPetPlayer;
import de.Keyle.MyPet.util.locale.MyPetLocales;
import de.Keyle.MyPet.util.logger.DebugLogger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public class MobArena
{
    public static boolean DISABLE_PETS_IN_ARENA = true;
    private static MobArenaHandler arenaHandler;
    private static boolean active = false;

    public static void findPlugin()
    {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("MobArena"))
        {
            arenaHandler = new MobArenaHandler();
            active = true;
        }
        DebugLogger.info("MobArena Support " + (active ? "" : "not ") + "activated.");
    }

    public static boolean isInMobArena(MyPetPlayer owner)
    {
        if (active && arenaHandler != null)
        {
            return arenaHandler.isPlayerInArena(owner.getPlayer());
        }
        return false;
    }

    @EventHandler
    public void onJoinPvPArenaEvent(ArenaPlayerJoinEvent event)
    {
        if (DISABLE_PETS_IN_ARENA && MyPetPlayer.isMyPetPlayer(event.getPlayer()))
        {
            MyPetPlayer player = MyPetPlayer.getMyPetPlayer(event.getPlayer());
            if (player.hasMyPet() && player.getMyPet().getStatus() == PetState.Here)
            {
                player.getMyPet().removePet();
                player.getPlayer().sendMessage(MyPetBukkitUtil.setColors(MyPetLocales.getString("Message.NotAllowedHere", player.getPlayer())));
            }
        }
    }
}