package com.example.zelenin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.zelenin.R
import com.example.zelenin.databinding.FragmentDevelopersLifeBinding
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class DevelopersLifeFragment : Fragment(R.layout.fragment_developers_life) {

    private lateinit var binding: FragmentDevelopersLifeBinding
    private val url: URL = URL("https://developerslife.ru/random?json=true")
    private var link: String? = ""
    private var description: String? = ""
    private var rightBorder = 0
    private var counter = 0
    private lateinit var images: MutableList<String>
    private lateinit var descriptions: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDevelopersLifeBinding.inflate(inflater)
        images = mutableListOf()
        descriptions = mutableListOf()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUrl(url)
        glideIt(link)
        binding.btnBack.isEnabled = false
        binding.btnNext.setOnClickListener()
        {
            getUrl(url)
            glideIt(link)
            if (counter != rightBorder) {
                ++counter
            }

            if (counter != -1) {
                binding.btnBack.isEnabled = true
            }
            binding.tvDescription.text = descriptions[counter]
            glideIt(images[counter])
        }

        binding.btnBack.setOnClickListener() {
            if (counter != 0 && counter != rightBorder) {
                binding.btnBack.isEnabled = true
                --counter
                if (counter == 0) {
                    binding.btnBack.isEnabled = false
                }
            } else {
                binding.btnBack.isEnabled = false
            }
            binding.tvDescription.text = descriptions[counter]
            glideIt(images[counter])
        }
    }

    private fun linkHandler(param: String?): String? {
        val jsonObject: JSONObject = JSONObject(param)
        var processedLink = ""
        try {
            processedLink = jsonObject.getString("gifURL")
            images.add(processedLink)
            rightBorder = images.lastIndex
            return processedLink
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return processedLink
    }

    private fun descriptionHandler(param: String?): String? {
        val jsonObject: JSONObject = JSONObject(param)
        var processedDescription = ""
        try {
            processedDescription = jsonObject.getString("description")
            descriptions.add(processedDescription)
            return processedDescription
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return processedDescription
    }

    private fun glideIt(link: String?) {
        Glide
            .with(this)
            .asGif()
            .load(link)
            .error(R.drawable.ic_image_error)
            .placeholder(R.drawable.ic_image_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(binding.imgGIF)
    }

    private fun getUrl(url: URL): String? {
        thread {
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                var inputStream = BufferedInputStream(urlConnection.inputStream)
                link = readStream(inputStream)
                description = descriptionHandler(link)
                link = linkHandler(link)
            } finally {
                urlConnection.disconnect()
            }
        }
        return link
    }

    private fun readStream(`is`: InputStream): String? {
        return try {
            val bo = ByteArrayOutputStream()
            var i = `is`.read()
            while (i != -1) {
                bo.write(i)
                i = `is`.read()
            }
            bo.toString()
        } catch (e: IOException) {
            ""
        }
    }

    override fun onDestroy() {
        Glide.get(requireContext()).clearMemory();
        Glide.get(requireContext()).clearDiskCache();
        super.onDestroy()
    }

    companion object {

        const val TAG = "DEVELOPERS_LIFE_FRAGMENT"
    }
}