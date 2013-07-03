package openperipheral.common.robotupgrades.lazers;

import java.util.HashMap;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.nbt.NBTTagCompound;
import openperipheral.api.IRobot;
import openperipheral.api.IRobotUpgradeInstance;

public class InstanceLazersUpgrade implements IRobotUpgradeInstance {

	private static final String TAG_OVERHEATED = "o";
	private static final String TAG_HEAT = "h";
	
	private IRobot robot;
	
	/**
	 * The current heat of the lazer
	 */
	private double heat = 0;
	
	/**
	 * The highest upgrade tier in the inventory
	 */
	private int tier = 0;
	
	/**
	 * Is the gun currently overheated
	 */
	private boolean isOverheated = false;
	
	
	public InstanceLazersUpgrade(IRobot robot, int tier) {
		this.robot = robot;
		this.tier = tier;
	}
	
	public IRobot getRobot() {
		return robot;
	}
	
	public int getTier() {
		return tier;
	}
	
	public EntityCreature getEntity() {
		return getRobot().getEntity();
	}
	
	public double getHeat() {
		return heat;
	}
	
	public void modifyHeat(double mod) {
		heat += mod;
		heat = Math.max(0, heat);
	}
	
	public boolean isCoolEnough() {
		return heat <= getMaxHeat();
	}
	
	public double getMaxHeat() {
		return tier * 15;
	}
	
	public double getCoolingPerTick() {
		return .1 + (tier * 0.02);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setDouble(TAG_HEAT, heat);
		nbt.setBoolean(TAG_OVERHEATED, isOverheated);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey(TAG_HEAT)) {
			heat = nbt.getDouble(TAG_HEAT);
		}
		if (nbt.hasKey(TAG_OVERHEATED)) {
			isOverheated = nbt.getBoolean(TAG_OVERHEATED);
		}
	}

	@Override
	public HashMap<Integer, EntityAIBase> getAITasks() {
		return null;
	}

	@Override
	public void update() {
		getRobot().modifyWeaponSpinSpeed(-0.01f);
		modifyHeat(-getCoolingPerTick());
		if (heat == 0 && isOverheated) {
			isOverheated = false;
		}
	}

	@Override
	public void onTierChanged(int tier) {
		this.tier = tier;
	}

	public boolean isOverheated() {
		return isOverheated;
	}

	public void setOverheated(boolean overheated) {
		isOverheated = overheated;
	}

}