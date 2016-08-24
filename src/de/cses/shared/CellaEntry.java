package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CellaEntry implements IsSerializable{
	
	private int caveID;
	private boolean dimensionsHeight;
	private boolean dimensionsDepth;
	private boolean dimensionsWidth;
	private boolean frontWall;
	private boolean frontWallLeftSide;
	private boolean frontWallRightSide;
	private boolean frontWallLunette;
	private boolean leftSideWall;
	private boolean leftSideWallLowerBorder;
	private boolean leftSideWallRegister;
	private boolean leftSideWallUpperBorder;
	private boolean rightSideWall;
	private boolean rightSideWallLowerBorder;
	private boolean rightSideWallRegister;
	private boolean rightSideWallUpperBorder;
	private boolean mainWall;
	private boolean mainWallNiches;
	private boolean mainWallPedestals;
	private boolean niches;
	private boolean pedestals;
	private boolean laternCeiling;
	private boolean copulaDome;
	private boolean vaultedCeiling;
	private boolean lozengeShapedMountainPattern;
	private int numberOfLozenges;
	private int numberOfJatakasAvadanas;
	private boolean buddhaInCentre;
	private boolean colouring;
	private boolean zenith;
	private boolean floor;
	public int getCaveID() {
		return caveID;
	}
	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}
	public boolean isDimensionsHeight() {
		return dimensionsHeight;
	}
	public void setDimensionsHeight(boolean dimensionsHeight) {
		this.dimensionsHeight = dimensionsHeight;
	}
	public boolean isDimensionsDepth() {
		return dimensionsDepth;
	}
	public void setDimensionsDepth(boolean dimensionsDepth) {
		this.dimensionsDepth = dimensionsDepth;
	}
	public boolean isDimensionsWidth() {
		return dimensionsWidth;
	}
	public void setDimensionsWidth(boolean dimensionsWidth) {
		this.dimensionsWidth = dimensionsWidth;
	}
	public boolean isFrontWall() {
		return frontWall;
	}
	public void setFrontWall(boolean frontWall) {
		this.frontWall = frontWall;
	}
	public boolean isFrontWallLeftSide() {
		return frontWallLeftSide;
	}
	public void setFrontWallLeftSide(boolean frontWallLeftSide) {
		this.frontWallLeftSide = frontWallLeftSide;
	}
	public boolean isFrontWallRightSide() {
		return frontWallRightSide;
	}
	public void setFrontWallRightSide(boolean frontWallRightSide) {
		this.frontWallRightSide = frontWallRightSide;
	}
	public boolean isFrontWallLunette() {
		return frontWallLunette;
	}
	public void setFrontWallLunette(boolean frontWallLunette) {
		this.frontWallLunette = frontWallLunette;
	}
	public boolean isLeftSideWall() {
		return leftSideWall;
	}
	public void setLeftSideWall(boolean leftSideWall) {
		this.leftSideWall = leftSideWall;
	}
	public boolean isLeftSideWallLowerBorder() {
		return leftSideWallLowerBorder;
	}
	public void setLeftSideWallLowerBorder(boolean leftSideWallLowerBorder) {
		this.leftSideWallLowerBorder = leftSideWallLowerBorder;
	}
	public boolean isLeftSideWallRegister() {
		return leftSideWallRegister;
	}
	public void setLeftSideWallRegister(boolean leftSideWallRegister) {
		this.leftSideWallRegister = leftSideWallRegister;
	}
	public boolean isLeftSideWallUpperBorder() {
		return leftSideWallUpperBorder;
	}
	public void setLeftSideWallUpperBorder(boolean leftSideWallUpperBorder) {
		this.leftSideWallUpperBorder = leftSideWallUpperBorder;
	}
	public boolean isRightSideWall() {
		return rightSideWall;
	}
	public void setRightSideWall(boolean rightSideWall) {
		this.rightSideWall = rightSideWall;
	}
	public boolean isRightSideWallLowerBorder() {
		return rightSideWallLowerBorder;
	}
	public void setRightSideWallLowerBorder(boolean rightSideWallLowerBorder) {
		this.rightSideWallLowerBorder = rightSideWallLowerBorder;
	}
	public boolean isRightSideWallRegister() {
		return rightSideWallRegister;
	}
	public void setRightSideWallRegister(boolean rightSideWallRegister) {
		this.rightSideWallRegister = rightSideWallRegister;
	}
	public boolean isRightSideWallUpperBorder() {
		return rightSideWallUpperBorder;
	}
	public void setRightSideWallUpperBorder(boolean rightSideWallUpperBorder) {
		this.rightSideWallUpperBorder = rightSideWallUpperBorder;
	}
	public boolean isMainWall() {
		return mainWall;
	}
	public void setMainWall(boolean mainWall) {
		this.mainWall = mainWall;
	}
	public boolean isMainWallNiches() {
		return mainWallNiches;
	}
	public void setMainWallNiches(boolean mainWallNiches) {
		this.mainWallNiches = mainWallNiches;
	}
	public boolean isMainWallPedestals() {
		return mainWallPedestals;
	}
	public void setMainWallPedestals(boolean mainWallPedestals) {
		this.mainWallPedestals = mainWallPedestals;
	}
	public boolean isNiches() {
		return niches;
	}
	public void setNiches(boolean niches) {
		this.niches = niches;
	}
	public boolean isPedestals() {
		return pedestals;
	}
	public void setPedestals(boolean pedestals) {
		this.pedestals = pedestals;
	}
	public boolean isLaternCeiling() {
		return laternCeiling;
	}
	public void setLaternCeiling(boolean laternCeiling) {
		this.laternCeiling = laternCeiling;
	}
	public boolean isCopulaDome() {
		return copulaDome;
	}
	public void setCopulaDome(boolean copulaDome) {
		this.copulaDome = copulaDome;
	}
	public boolean isVaultedCeiling() {
		return vaultedCeiling;
	}
	public void setVaultedCeiling(boolean vaultedCeiling) {
		this.vaultedCeiling = vaultedCeiling;
	}
	public boolean isLozengeShapedMountainPattern() {
		return lozengeShapedMountainPattern;
	}
	public void setLozengeShapedMountainPattern(boolean lozengeShapedMountainPattern) {
		this.lozengeShapedMountainPattern = lozengeShapedMountainPattern;
	}
	public int getNumberOfLozenges() {
		return numberOfLozenges;
	}
	public void setNumberOfLozenges(int numberOfLozenges) {
		this.numberOfLozenges = numberOfLozenges;
	}
	public int getNumberOfJatakasAvadanas() {
		return numberOfJatakasAvadanas;
	}
	public void setNumberOfJatakasAvadanas(int numberOfJatakasAvadanas) {
		this.numberOfJatakasAvadanas = numberOfJatakasAvadanas;
	}
	public boolean isBuddhaInCentre() {
		return buddhaInCentre;
	}
	public void setBuddhaInCentre(boolean buddhaInCentre) {
		this.buddhaInCentre = buddhaInCentre;
	}
	public boolean isColouring() {
		return colouring;
	}
	public void setColouring(boolean colouring) {
		this.colouring = colouring;
	}
	public boolean isZenith() {
		return zenith;
	}
	public void setZenith(boolean zenith) {
		this.zenith = zenith;
	}
	public boolean isFloor() {
		return floor;
	}
	public void setFloor(boolean floor) {
		this.floor = floor;
	}
	
	

}
