package model;

public class ImportExport
{
    private String importPath;
    private String filename;
    private String Sachbearbeiter;
    private String Vermittler;

    public ImportExport(String importPath, String filename, String sachbearbeiter, String vermittler)
    {
        this.importPath = importPath;
        this.filename = filename;
        Sachbearbeiter = sachbearbeiter;
        Vermittler = vermittler;
    }

    public String getImportPath() {
        return importPath;
    }

    public void setImportPath(String importPath) {
        this.importPath = importPath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSachbearbeiter() {
        return Sachbearbeiter;
    }

    public void setSachbearbeiter(String sachbearbeiter) {
        Sachbearbeiter = sachbearbeiter;
    }

    public String getVermittler() {
        return Vermittler;
    }

    public void setVermittler(String vermittler) {
        Vermittler = vermittler;
    }
}
