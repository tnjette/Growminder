package com.tylerjette.growmindv05;

import android.util.Log;

public class getReadableEventTitle {

    private String finalTitleString;
    private String roughStr;

    public getReadableEventTitle(String roughTitle) {

        this.roughStr = roughTitle;

        switch (roughStr) {
            case "fertilizeBed" :
                this.finalTitleString = "Fertilize garden bed";
                break;
            case "startInside":
                this.finalTitleString = "Indoor seeding";
                break;
            case "startHardenOff":
                this.finalTitleString = "Hardening off seedlings";
                break;
            case "startOutside":
                this.finalTitleString = "Transplanting";
                break;
            case "startOutsideCovered":
                this.finalTitleString = "Transplanting";
                break;
            case "stopOutside":
                this.finalTitleString = "End planting";
                break;
            case "startHarvest":
                this.finalTitleString = "Harvest";
                break;
            case "stopHarvest" :
                this.finalTitleString = "Stop harvest";
                break;
            case "directOutside":
                this.finalTitleString = "Planting";
                break;
            case "directOutsideCovered":
                this.finalTitleString = "Planting";
                break;
            case "firstMulch":
                this.finalTitleString = "Mulching";
                break;
            case "transplantToLargerContainer":
                this.finalTitleString = "Transplant";
                break;
            case "fertilizePlantsFirstTime":
                this.finalTitleString = "Fertilize";
                break;
            case "fertilizePlantsSecondTime":
                this.finalTitleString = "Fertilize, again";
                break;
            case "fertilizePlantsThirdTime" :
                this.finalTitleString = "Fertilize, again";
                break;
            case "thinSeedlings" :
                this.finalTitleString = "Thin seedlings";
                break;
            case "firstThin":
                this.finalTitleString = "Thin seedlings";
                break;
            case "secondMulch":
                this.finalTitleString = "Mulch, again";
                break;
            case "removeMulch" :
                this.finalTitleString = "Remove mulch";
                break;
            case "secondThin":
                this.finalTitleString = "Thin seedlings, again";
                break;
            case "cutBack":
                this.finalTitleString = "Trim";
                break;
            case "firstBlanch":
                this.finalTitleString = "Blanch";
                break;
            case "secondBlanch":
                this.finalTitleString = "Blanch, again";
                break;
            case "blanch" :
                this.finalTitleString = "Blanch";
                break;
            case "changeWaterCycle":
                this.finalTitleString = "Change water cycle";
                break;
            case "yr2fallFertilizer" :
                this.finalTitleString = "Apply fertilizer, again";
                break;
            case "fallFertilizer" :
                this.finalTitleString = "Apply fertilizer";
                break;
            case "springFertilizer" :
                this.finalTitleString = "Apply fertilizer";
                break;
            case "yr2springFertilizer" :
                this.finalTitleString = "Apply fertilizer, again";
                break;
            //second round
            case "fertilizeBed2" :
                this.finalTitleString = "Fertilize garden bed (Fall cycle)";
                break;
            case "startInside2":
                this.finalTitleString = "Indoor seeding (Fall cycle)";
                break;
            case "startHardenOff2":
                this.finalTitleString = "Hardening off seedlings (Fall cycle)";
                break;
            case "startOutside2":
                this.finalTitleString = "Transplanting (Fall cycle)";
                break;
            case "startOutside2Covered":
                this.finalTitleString = "Transplanting (Fall cycle)";
                break;
            case "stopOutside2":
                this.finalTitleString = "End planting (Fall cycle)";
                break;
            case "startHarvest2":
                this.finalTitleString = "Harvest (Fall cycle)";
                break;
            case "stopHarvest2" :
                this.finalTitleString = "Stop harvest (Fall cycle)";
                break;
            case "directOutside2":
                this.finalTitleString = "Planting (Fall cycle)";
                break;
            case "directOutside2Covered":
                this.finalTitleString = "Planting (Fall cycle)";
                break;
            case "firstMulch2":
                this.finalTitleString = "Mulching (Fall cycle)";
                break;
            case "transplantToLargerContainer2":
                this.finalTitleString = "Transplant (Fall cycle)";
                break;
            case "fertilizePlantsFirstTime2":
                this.finalTitleString = "Fertilize (Fall cycle)";
                break;
            case "fertilizePlantsSecondTime2":
                this.finalTitleString = "Fertilize, again (Fall cycle)";
                break;
            case "fertilizePlantsThirdTime2" :
                this.finalTitleString = "Fertilize, again (Fall cycle)";
                break;
            case "firstThin2":
                this.finalTitleString = "Thin seedlings (Fall cycle)";
                break;
            case "secondMulch2":
                this.finalTitleString = "Mulch, again (Fall cycle)";
                break;
            case "removeMulch2" :
                this.finalTitleString = "Remove mulch (Fall cycle)";
                break;
            case "secondThin2":
                this.finalTitleString = "Thin seedlings, again (Fall cycle)";
                break;
            case "cutBack2":
                this.finalTitleString = "Trim (Fall cycle)";
                break;
            case "blanch2" :
                this.finalTitleString = "Blanch (Fall cycle)";
                break;
            case "firstBlanch2":
                this.finalTitleString = "Blanch (Fall cycle)";
                break;
            case "secondBlanch2":
                this.finalTitleString = "Blanch, again (Fall cycle)";
                break;
            case "changeWaterCycle2":
                this.finalTitleString = "Change water cycle (Fall cycle)";
                break;
            case "yr2fallFertilizer2" :
                this.finalTitleString = "Apply fertilizer, again (Fall cycle)";
                break;
            case "fallFertilizer2" :
                this.finalTitleString = "Apply fertilizer (Fall cycle)";
                break;
            case "springFertilizer2" :
                this.finalTitleString = "Apply fertilizer (Fall cycle)";
                break;
            case "yr2springFertilizer2" :
                this.finalTitleString = "Apply fertilizer, again (Fall cycle)";
                break;
            case "EXPIRED" :
                this.finalTitleString = "Planting window has passed";
                break;
            case "HAS NOT BEGUN" :
                this.finalTitleString = "Planting window has not begun";
                break;
            default:
                this.finalTitleString = "";
                break;
        }
    }
    public String getFinalTitleStr(){return finalTitleString;}
}
