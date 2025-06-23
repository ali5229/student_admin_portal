package com.example.studata;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;
public class StudentListAdapter extends BaseAdapter {

    private Context context;
    private List<Student> studentList;

    public StudentListAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        }

        Student student = studentList.get(position);

        TextView nameTextView = convertView.findViewById(R.id.student_name);
        TextView emailTextView = convertView.findViewById(R.id.student_email);
        TextView cgpaTextView = convertView.findViewById(R.id.student_cgpa);
        TextView rollTextView = convertView.findViewById(R.id.student_roll);
        TextView semesterTextView = convertView.findViewById(R.id.student_semester);
        ImageView imageView = convertView.findViewById(R.id.student_image);

        nameTextView.setText("Name: " + student.getName());
        emailTextView.setText("Email: "+ student.getEmail());
        cgpaTextView.setText("CGPA: " + student.getCgpa());
        rollTextView.setText("Roll: " + student.getRoll());
        semesterTextView.setText("Semester: " + student.getSemester());

        Glide.with(context).load(student.getImageUrl()).into(imageView);

        return convertView;
    }
}
