/*
 * This file is part of MyPet
 *
 * Copyright © 2011-2019 Keyle
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

package de.Keyle.MyPet.util.hooks;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import de.Keyle.MyPet.MyPetApi;
import de.Keyle.MyPet.api.util.hooks.PluginHookName;
import de.Keyle.MyPet.api.util.hooks.types.FlyHook;
import de.Keyle.MyPet.api.util.hooks.types.PlayerVersusEntityHook;
import de.Keyle.MyPet.api.util.hooks.types.PlayerVersusPlayerHook;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@PluginHookName("Residence")
public class ResidenceHook implements PlayerVersusPlayerHook, PlayerVersusEntityHook, FlyHook {

    Residence residence;

    @Override
    public boolean onEnable() {
        if (MyPetApi.getPluginHookManager().getConfig().getConfig().getBoolean("Residence.Enabled")) {
            residence = MyPetApi.getPluginHookManager().getPluginInstance(Residence.class).get();

            FlagPermissions.addFlag("mypet-fly");
            FlagPermissions.addFlag("mypet-damage");
            return true;
        }
        return false;
    }

    @Override
    public boolean canHurt(Player attacker, Player defender) {
        try {
            FlagPermissions flagPermissions = residence.getPermsByLoc(defender.getLocation());
            return flagPermissions.has(Flags.pvp, true) && flagPermissions.has("mypet-damage", true);
        } catch (Throwable ignored) {
        }
        return true;
    }

    @Override
    public boolean canFly(Location location) {
        try {
            FlagPermissions flagPermissions = residence.getPermsByLoc(location);
            return flagPermissions.has("mypet-fly", true);
        } catch (Throwable ignored) {
        }
        return true;
    }

    @Override
    public boolean canHurt(Player attacker, Entity defender) {
        try {
            FlagPermissions flagPermissions = residence.getPermsByLoc(defender.getLocation());
            return flagPermissions.has("mypet-damage", true);
        } catch (Throwable ignored) {
        }
        return true;
    }
}