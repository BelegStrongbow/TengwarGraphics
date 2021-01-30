package tengwarGraphics;

public class Filter{
    int index;
    FilterEnum filterType;
    double weight;
    Integer[][] kernel;

    public Filter(int index, FilterEnum filterType, double weight, Integer[][] kernel){
        this.index=index;
        this.filterType = filterType;
        this.weight = weight;
        this.kernel = kernel;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public FilterEnum getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterEnum filterType) {
        this.filterType = filterType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Integer[][] getKernel() {
        return kernel;
    }

    public void setKernel(Integer[][] kernel) {
        this.kernel = kernel;
    }
}