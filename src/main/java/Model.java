public class Model {
    private String email;
    private String comment;

    public Model(String email, String comment){
        this.email = email;
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public String getComment() {
        return comment;
    }
}
