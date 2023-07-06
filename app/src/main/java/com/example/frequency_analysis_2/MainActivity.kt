package com.example.frequency_analysis_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.frequency_analysis_2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding      // Объявляем переменную, которая инициализируются позже в коде, которую в последствии можно использовать на протяжении всего класса
    private var count: Int = 0                         // Задаем переменную для подсчета, которую можно использовать во всем классе

    override fun onCreate(savedInstanceState: Bundle?) {        // Функция создания экрана приложения
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)     // Связывание основного кода с XML-разметкой(дизайн приложения). Преобразует XML-файл в объекты View, которые можно использовать в основном коде
        val view = binding.root
        setContentView(view)

        binding.encryptButton.setOnClickListener {                 // Действия, которые происходят при нажатии на кнопку заменить(находим кнопку в XML-файле с помощью binding)
            val inputText: String = binding.inputEditText.text.toString()          // Задаем переменную, которая преобразует весь введеный пользователем текст в строку
            val encryptedText = encryptText(inputText)                      // Задаем переменную encryptedText, которая хранит в себе функцию encryptText() по введенному пользователем тексту(inputText)
            binding.outputEditText.setText(encryptedText)                       // Устанавливаем зашифрованный encryptedText, в вывод зашифрованного текста(XML-файл)
            count = 0                                       // Каждый раз при нажатии кнопки устанавливаем count = 0, чтобы избежать постоянного прибавления count
        }
    }

    private fun encryptText(input: String): String {               // Создаем функцию, которая принимает любой текст и по условиям шифрует его
        var encryptedText = input                                       // Создаем переменную encryptedText и передаем ей введеный пользователем текст

        if (binding.checkboxSo.isChecked) {                                                          // Если чекбокс нажат
            encryptedText = encryptedText.replace("со", "8-7", ignoreCase = true)    // Используя встроенную функцию(.replace) заменяем введеный текст пользователем на нужные нам значения без учета регистра(большие или маленькие символы)
            countMatches("8-7", encryptedText)                                             // Используем функцию countMatches, которой передаем строку и текст по которому нужно искать
        }

        if (binding.checkboxKo.isChecked) {
            encryptedText = encryptedText.replace("ко", "\uD83D\uDC99", ignoreCase = true)
            countMatches("\uD83D\uDC99", encryptedText)
        }

        if (binding.checkboxVe.isChecked) {
            encryptedText = encryptedText.replace("ве", "4+4", ignoreCase = true)
            countMatches("4\\+4", encryptedText)
        }

        if (binding.checkboxSya.isChecked) {
            encryptedText = encryptedText.replace("ся", "1=1", ignoreCase = true)
            countMatches("1=1", encryptedText)
        }

        if (binding.checkboxNya.isChecked) {
            encryptedText = encryptedText.replace("ня", "2*2", ignoreCase = true)
            countMatches("2\\*2", encryptedText)
        }

        if (binding.checkboxV.isChecked) {
            encryptedText = encryptedText.replace("в", "*", ignoreCase = true)
            countMatches("\\*", encryptedText)
        }

        if (binding.checkboxR.isChecked) {
            encryptedText = encryptedText.replace("р", "%", ignoreCase = true)
            countMatches("%", encryptedText)
        }

        if (binding.checkboxE.isChecked) {
            encryptedText = encryptedText.replace("е", "№", ignoreCase = true)
            countMatches("№", encryptedText)
        }

        if (binding.checkboxT.isChecked){
            encryptedText = encryptedText.replace("т", "#", ignoreCase = true)
            countMatches("#", encryptedText)
        }

        if (binding.checkboxC.isChecked) {
            encryptedText = encryptedText.replace("с", "@", ignoreCase = true)
            countMatches("@", encryptedText)
        }


        binding.textViewCount.text = "Количество замененных символов: $count (${formattedPercentage(count, encryptedText)}%)"      // С помощью $count в тексте обращаемся к переменной а затем в скобках обращаемся к функции formattedPercentage, которая принимает число по которому нужно найти процент и текст

        val countSpaces = encryptedText.count { it == ' ' }                                                                        // Записывает в переменную countSpaces количество пробелов. Используем .count и передаем ей лямбда-функцию, которая передает функции count значение, которое нужно подсчитать
        binding.textViewSpaces.text = "Количество пробелов: $countSpaces (${formattedPercentage(countSpaces, encryptedText)}%)"

        val countComma = encryptedText.count { it == ',' }
        binding.textViewComma.text = "Количество запятых $countComma (${formattedPercentage(countComma, encryptedText)}%)"

        val totalWords = encryptedText.trim().split("\\s".toRegex()).size
        binding.textCountWords.text = "Количество слов: $totalWords"

        val totalSymbols = encryptedText.replace("\\s".toRegex(), "").length
        binding.textCountSymbols.text = "Количество символов: $totalSymbols"


        return encryptedText           // Возвращаем зашифрованный текст
    }

   private fun countMatches(regexPattern: String, encryptedText: String): Int {   // Функция подсчета совпадений
        val regex = Regex(regexPattern)                                           // Создаем регулярное выражение(строки, на которые мы меняем)
        val matches = regex.findAll(encryptedText)                              // По замененному тексту ищем все совпадения
        matches.forEach {                                              // Пробегаемся по каждому совпадению и записываем каждое в count
            count++
        }
        return count                              // Возвращаем подсчитанное значение
    }

    private fun formattedPercentage(number: Int, encryptedText: String): String {               // Функция рассчета процентного соотношения по тексту и форматирования все в формат с двумя знаками после запятой
        val percentage: Double = (number.toDouble() * 100) / encryptedText.length.toDouble()    // Подсчет процентов по передаваемому number
        return String.format("%.2f", percentage)                                                // Форматируем строку до двух символов после запятой и возвращаем ее
    }

}

