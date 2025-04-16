import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mohammedezzaim.demo.Student

class StudentDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "students.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE students (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT,
                status TEXT,
                img_path TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS students")
        onCreate(db)
    }

    fun insertStudent(nom: String, status: String, imgPath: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nom", nom)
            put("status", status)
            put("img_path", imgPath)
        }
        val result = db.insert("students", null, values)
        return result != -1L
    }

    fun getAllStudents(): List<Student> {
        val list = mutableListOf<Student>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM students", null)

        while (cursor.moveToNext()) {
            val nom = cursor.getString(cursor.getColumnIndexOrThrow("nom"))
            val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))
            val imgPath = cursor.getString(cursor.getColumnIndexOrThrow("img_path"))
            list.add(Student(nom, status, imgPath))
        }

        cursor.close()
        return list
    }

    fun getStudentsByName(nomQuery: String): List<Triple<String, String, String>> {
        val list = mutableListOf<Triple<String, String, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM students WHERE nom = ?", arrayOf(nomQuery))

        while (cursor.moveToNext()) {
            val nom = cursor.getString(cursor.getColumnIndexOrThrow("nom"))
            val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))
            val imgPath = cursor.getString(cursor.getColumnIndexOrThrow("img_path"))
            list.add(Triple(nom, status, imgPath))
        }

        cursor.close()
        return list
    }
}