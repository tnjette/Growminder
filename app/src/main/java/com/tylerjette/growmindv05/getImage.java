package com.tylerjette.growmindv05;

public class getImage {
    private String imageName;
    private int varietyImage;

    public getImage(String imageName){

        this.imageName = imageName;

        switch(imageName) {
            case "artichoke":
                this.varietyImage = R.drawable.artichoke;
                break;
            case "arugula":
                this.varietyImage = R.drawable.arugula;
                break;
            case "asparagus":
                this.varietyImage = R.drawable.asparagus;
                break;
            case "basil":
                this.varietyImage = R.drawable.basil;
                break;
            case "beanssnap":
                this.varietyImage = R.drawable.beanssnap;
                break;
            case "beansshell":
                this.varietyImage = R.drawable.beansshell;
                break;
            case "broccoli":
                this.varietyImage = R.drawable.broccoli;
                break;
            case "beets":
                this.varietyImage = R.drawable.beets;
                break;
            case "brusselssprouts":
                this.varietyImage = R.drawable.brusselssprouts;
                break;
            case "cabbage":
                this.varietyImage = R.drawable.cabbage;
                break;
            case "carrots":
                this.varietyImage = R.drawable.carrots;
                break;
            case "cauliflower":
                this.varietyImage = R.drawable.cauliflower;
                break;
            case "celery":
                this.varietyImage = R.drawable.celery;
                break;
            case "chard":
                this.varietyImage = R.drawable.chard;
                break;
            case "chinesecabbage":
                this.varietyImage = R.drawable.chinesecabbage;
                break;
            case "chives":
                this.varietyImage = R.drawable.chives;
                break;
            case "cilantro":
                this.varietyImage = R.drawable.cilantro;
                break;
            case "collardgreens":
                this.varietyImage = R.drawable.collardgreens;
                break;
            case "cornsweet":
                this.varietyImage = R.drawable.corn;
                break;
            case "cucumberspickle":
                this.varietyImage = R.drawable.cucumberpickle;
                break;
            case "cucumbersslicing":
                this.varietyImage = R.drawable.cucumbersslicing;
                break;
            case "cantaloupes":
                this.varietyImage = R.drawable.cantaloupes;
                break;
            case "dill":
                this.varietyImage = R.drawable.dill;
                break;
            case "eggplants":
                this.varietyImage = R.drawable.eggplant;
                break;
            case "endive":
                this.varietyImage = R.drawable.endive;
                break;
            case "garlic":
                this.varietyImage = R.drawable.garlic;
                break;
            case "kale":
                this.varietyImage = R.drawable.kale;
                break;
            case "kohlrabi":
                this.varietyImage = R.drawable.kohlrabi;
                break;
            case "lettucehead":
                this.varietyImage = R.drawable.lettucehead;
                break;
            case "lettuceleaf":
                this.varietyImage = R.drawable.lettuceleaf;
                break;
            case "leeks":
                this.varietyImage = R.drawable.leeks;
                break;
            case "onions":
                this.varietyImage = R.drawable.onion;
                break;
            case "okra":
                this.varietyImage = R.drawable.okra;
                break;
            case "parsley":
                this.varietyImage = R.drawable.parsley;
                break;
            case "parsnips":
                this.varietyImage = R.drawable.parsnips;
                break;
            case "peas":
                this.varietyImage = R.drawable.peas;
                break;
            case "peppershot":
                this.varietyImage = R.drawable.peppershot;
                break;
            case "pepperssweet":
                this.varietyImage = R.drawable.pepperssweet;
                break;
            case "potatoeswhite":
                this.varietyImage = R.drawable.potatowhite;
                break;
            case "pumpkins":
                this.varietyImage = R.drawable.pumpkin;
                break;
            case "radish":
                this.varietyImage = R.drawable.radish;
                break;
            case "rhubarb":
                this.varietyImage = R.drawable.rhubarb;
                break;
            case "spinach":
                this.varietyImage = R.drawable.spinach;
                break;
            case "squashsummer":
                this.varietyImage = R.drawable.zuccini;
                break;
            case "squashwinter":
                this.varietyImage = R.drawable.squashwinter;
                break;
            case "tomatillos":
                this.varietyImage = R.drawable.tomatillo;
                break;
            case "tomatoes":
                this.varietyImage = R.drawable.tomato;
                break;
            case "turnips":
                this.varietyImage = R.drawable.turnips;
                break;
            case "watermelons":
                this.varietyImage = R.drawable.watermelon;
                break;
            default:
                this.varietyImage = R.drawable.cake_1;
                break;
        }
    }
    public int getImage(){
        return varietyImage;
    }
}
