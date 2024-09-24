package lk.learningApp.model;

public class Student {
    private String uid;
    private String name;
    private String email;
    private int age;

    public Student(String uid, String name, String email, int age) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

//    @Override
//    public String toString() {
//        return "Student{" +
//                "uid='" + uid + '\'' +
//                ", name='" + name + '\'' +
//                ", email='" + email + '\'' +
//                ", age=" + age +
//                '}';
//    }
}
