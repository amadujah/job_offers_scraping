package offres_emploi;

import dev.morphia.annotations.*;
import org.bson.types.ObjectId;

import java.util.Date;

@Entity("jobs")
public class Job {
    @Id
    private ObjectId id;
    private String title;
    private String description;
    private String location;
    private String recruiter;
    private String url;
    private String reference;
    private Date date;

    public Job(String title, String description, String location, String recruiter,
               String url, String reference, Date date) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.recruiter = recruiter;
        this.url = url;
        this.reference = reference;
        this.date = date;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(String recruiter) {
        this.recruiter = recruiter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
