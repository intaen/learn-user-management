import android.content.Context
import android.content.SharedPreferences
import com.intanmarsjaf.bengongapps.model.User

class sharedPreferences(context: Context) {
    var _token="_TOKEN"

    private val sharedPrefFile="SHARED_PREFERENCES"

    var preferences: SharedPreferences
    var editor: SharedPreferences.Editor

    init {
        this.preferences=context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        this.editor=this.preferences.edit()
    }

    val isLoggedIn: Boolean
        get() {
            return preferences.getInt("id", -1) != -1
        }

    fun clear(){
        editor.clear()
        editor.apply()
    }

    fun remove(key:String){
        editor.remove(key)
        editor.apply()
        editor.commit()
    }

    fun saveInt(key: String,value: Int){
        editor.putInt(key,value)
        editor.apply()
        editor.commit()
    }

    fun saveString(key: String, value: String?){
        editor.putString(key,value)
        editor.apply()
        editor.commit()
    }

    fun saveUser(user: User) {
        editor.putInt("id", user.id)
        editor.putString("name", user.name)
        editor.putString("username", user.username)
        editor.putString("email", user.email)
        editor.putString("password", user.password)
        editor.apply()
    }

    fun getString(key: String): String?{
        return preferences.getString(key,"empty")
    }

    fun getInt(key: String):Int{
        return preferences.getInt(key,0)
    }
}