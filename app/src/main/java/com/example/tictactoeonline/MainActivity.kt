package com.example.tictactoeonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tictactoeonline.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {


    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playOfflineBtn.setOnClickListener{
            createOfflineGame()

        }
        binding.createOnlineGameBtn.setOnClickListener{
            createOnlineGame()
        }
        binding.joinOnlineGameBtn.setOnClickListener{
            joinOnlineGame()
        }


    }


    fun createOfflineGame(){
        GameData.saveGameModel(
            GameModel(
            gameStatus = GameStatus.JOINED
            )
        )
        startGame()

    }

    fun createOnlineGame(){
        GameData.myId = "X"
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.CREATED,
                gameId = Random.nextInt(1000..9999).toString()
            )
        )
        startGame()



    }
    fun joinOnlineGame(){
        var gameId = binding.gameIdInput.text.toString()
        if (gameId.isEmpty()){
            binding.gameIdInput.setError("Please enter game ID")
            return
        }
        GameData.myId = "O"
        Firebase.firestore.collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener {
             val model = it?.toObject(GameModel::class.java)
                if (model==null){
                    binding.gameIdInput.setError("Please enter valid game ID")

                }else{
                    model.gameStatus = GameStatus.JOINED
                    GameData.saveGameModel(model)
                    startGame()
                }
            }

    }

    fun startGame(){
        startActivity(Intent(this,GameActivity::class.java))
    }
}