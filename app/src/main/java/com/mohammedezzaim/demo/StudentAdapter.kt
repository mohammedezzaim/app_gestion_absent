import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.mohammedezzaim.demo.DetailActivity
import com.mohammedezzaim.demo.R
import com.mohammedezzaim.demo.Student
import java.io.File

class StudentAdapter(
    private val context: Context,
    private val resource: Int,
    private val students: ArrayList<Student>,
) : ArrayAdapter<Student>(context, resource, students) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val student = getItem(position) as Student

        val item = LayoutInflater.from(context).inflate(resource, parent, false)

        val name = item.findViewById<TextView>(R.id.name)
        val image = item.findViewById<ImageView>(R.id.image_profile)
        val absentOrPresent = item.findViewById<TextView>(R.id.status)
        val btnStatus = item.findViewById<Button>(R.id.absent_present)
        val btnDetails = item.findViewById<Button>(R.id.details)

        name.text = students[position].nom
        absentOrPresent.text = students[position].status

        if (students[position].status == "Present") {
            absentOrPresent.text = "Present"
            btnStatus.text = "Absent"
            btnStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.red_absent))
            absentOrPresent.setTextColor(ContextCompat.getColor(context, R.color.green_present))
        } else {
            absentOrPresent.text = "Absent"
            btnStatus.text = "Present"
            btnStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.green_present))
            absentOrPresent.setTextColor(ContextCompat.getColor(context, R.color.red_absent))
        }

        btnStatus.setOnClickListener {
            if (students[position].status == "Present") {
                students[position].status = "Absent"

                absentOrPresent.text = "Absent"
                btnStatus.text = "Present"
                btnStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.green_present))
                absentOrPresent.setTextColor(ContextCompat.getColor(context, R.color.red_absent))
            } else {
                students[position].status = "Present"
                absentOrPresent.text = "Present"
                btnStatus.text = "Absent"
                btnStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.red_absent))
                absentOrPresent.setTextColor(ContextCompat.getColor(context, R.color.green_present))
            }
        }

        if (!student.imagePath.isNullOrEmpty()) {
            val imageFile = File(student.imagePath)
            if (imageFile.exists()) {
                image.setImageURI(Uri.fromFile(imageFile))
            } else {
                image.setImageResource(R.drawable.image_profile) // Image par défaut
            }
        } else {
            image.setImageResource(R.drawable.image_profile)
        }


        btnDetails.setOnClickListener({

            val intentDetails = Intent(context, DetailActivity::class.java)
            intentDetails.putExtra("nom",students[position].nom)
            intentDetails.putExtra("status",students[position].status)
            intentDetails.putExtra("imagePath",students[position].imagePath)
            context.startActivity(intentDetails)

            Toast.makeText(context,students[position].nom, Toast.LENGTH_LONG).show();
        })

        return item
    }
}