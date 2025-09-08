package com.example.vandrservices.ui.form.BeforeReport

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.vandrservices.R
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BeforeReportFragment : Fragment() {
    private val viewModel: BeforeReportViewModel by viewModels()
    private lateinit var containerLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_before_report, container, false)
        containerLayout = view.findViewById(R.id.dynamic_form_container)
        val variety = arguments?.getString("variety") ?: "Banana"
        val lotIdString = arguments?.getString("lotId") ?: ""
        val lotId = lotIdString.toIntOrNull() ?: 0
        val localLotId = arguments?.getString("localLotId") ?: ""
        val grower = arguments?.getString("grower") ?: ""
        val packDate = arguments?.getString("packDate") ?: ""
        val cases = arguments?.getString("cases") ?: "0"
        val label = arguments?.getString("label") ?: ""

        viewModel.setLotPersist(variety, lotId, localLotId, grower, packDate, cases, label)
        val lotPersist = viewModel.getLotPersist()

        val bundel = bundleOf(
            "variety" to lotPersist["variety"],
            "lotId" to lotPersist["lotId"],
            "localLotId" to lotPersist["localLotId"],
            "grower" to lotPersist["grower"],
            "packDate" to lotPersist["packDate"],
            "cases" to lotPersist["cases"],
            "label" to lotPersist["label"]
        )
        Log.i(
            "BeforeReportFragment",
            "variety: $variety, lotId: $lotId, localLotId: $localLotId, grower: $grower, packDate: $packDate, cases: $cases, label: $label")

        view.findViewById<Button>(R.id.btn_new_palet).setOnClickListener {
            findNavController().navigate(R.id.paletCreationFragment, bundel)
        }
        view.findViewById<Button>(R.id.btn_report).setOnClickListener {
            val id = lotPersist["lotId"]
            Log.i("BeforeReportFragment", "id: $id")
            val url = "https://vandservices-af6ea8b693af.herokuapp.com/generate_pdf_strawberrie/?id=$id"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            view.context.startActivity(intent)
        }
        view.findViewById<Button>(R.id.btn_download).setOnClickListener {
            val id = lotPersist["lotId"]
            val url = "https://vandservices-af6ea8b693af.herokuapp.com/generate_pdf_strawberrie/?id=$id"

            downloadPdf(view.context, url, "report_$id.pdf") { file ->
                if (file != null) {
                    // El PDF se guardó en file.path, puedes notificar al usuario
                    (view.context as? Activity)?.runOnUiThread {
                        Toast.makeText(view.context, "PDF descargado en: ${file.path}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    (view.context as? Activity)?.runOnUiThread {
                        Toast.makeText(view.context, "Error al descargar el PDF", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        return view
    }

    fun downloadPdf(context: Context, url: String, fileName: String, onFinished: (File?) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFinished(null)
            }
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful && response.body != null) {
                    val inputStream = response.body!!.byteStream()
                    val file = File(context.getExternalFilesDir(null), fileName)
                    val outputStream = FileOutputStream(file)
                    inputStream.copyTo(outputStream)
                    outputStream.close()
                    inputStream.close()
                    onFinished(file)
                } else {
                    onFinished(null)
                }
            }
        })
    }

}