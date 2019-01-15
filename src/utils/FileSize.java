package utils;

public enum FileSize {
    MB_1(1),
    MB_10(10);
// MB_100(100);

    int mBytes;

    FileSize(int mBytes) {
        this.mBytes = mBytes;
    }

    public int getMegaBytes() {
        return mBytes;
    }

    public int getBytes() {
        return mBytes * 1024 * 1024;
    }

    public String getFileName() {
        return name() + ".txt";
    }

}
