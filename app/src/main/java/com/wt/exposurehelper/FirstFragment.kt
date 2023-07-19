package com.wt.exposurehelper

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.wt.exposurehelper.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding

    companion object {
        fun newInstance(type: String): FirstFragment {
            val frg = FirstFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            frg.arguments = bundle
            return frg
        }
    }

    private lateinit var mData: ArrayList<String>

    private val type by lazy {
        arguments?.getString("type") ?: ""
    }

    private lateinit var recyclerViewExposureHelper: RecyclerViewExposureHelper<String>

    private lateinit var adapter: FirstAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mData = arrayListOf()
        for (i in 1..30) {
            mData.add("$type-$i")
        }

        adapter = FirstAdapter(context, mData)

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter

        recyclerViewExposureHelper =
            RecyclerViewExposureHelper(
                recyclerView = binding.recyclerView,
                exposureValidAreaPercent = 20,
                lifecycleOwner = this,
                mayBeCoveredViewList = null,
                exposureStateChangeListener = object : IExposureStateChangeListener<String> {
                    override fun onExposureStateChange(
                        bindExposureData: String,
                        position: Int,
                        inExposure: Boolean,
                        duration: Long
                    ) {
                        Log.e(
                            "ListActivity", "${bindExposureData}---${
                                if (inExposure) {
                                    "开始曝光"
                                } else {
                                    "结束曝光"
                                }
                            }"
                        )
                    }
                })

        //长按删除
        adapter.setOnItemClickListener(object : FirstAdapter.OnItemClickListener {
            override fun onLongClick(view: View?, position: Int) {
                val data = adapter.data[position]
                recyclerViewExposureHelper.removeExposureData(position, data)
                adapter.removeData(position)
            }
        })
    }

}
