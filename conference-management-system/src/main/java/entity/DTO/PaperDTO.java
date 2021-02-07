package entity.DTO;

import java.util.List;

public class PaperDTO {
    int uploadedAuthorId;
    String tile;
    String Abstract;
    String keyword;
    String subject;
    List<Integer> authorIds;

    public PaperDTO() {
    }

    public PaperDTO(int uploadedAuthorId, String tile,
                    String anAbstract, String keyword, String subject, List<Integer> authorIds) {
        this.uploadedAuthorId = uploadedAuthorId;
        this.tile = tile;
        Abstract = anAbstract;
        this.keyword = keyword;
        this.subject = subject;
        this.authorIds = authorIds;
    }

    public int getUploadedAuthorId() {
        return uploadedAuthorId;
    }

    public void setUploadedAuthorId(int uploadedAuthorId) {
        this.uploadedAuthorId = uploadedAuthorId;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getAbstract() {
        return Abstract;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Integer> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<Integer> authorIds) {
        this.authorIds = authorIds;
    }
}
