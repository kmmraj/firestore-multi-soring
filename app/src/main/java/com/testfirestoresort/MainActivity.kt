package com.testfirestoresort

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Query.Direction
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private var mFirestore: FirebaseFirestore? = null
    private var mQuery: Query? = null
    private var mDeveloperListAdapter: DevelopersRVAdapter? = null

    private val mRVDevelopers: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recycler_developers)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseFirestore.setLoggingEnabled(true)


        initFirestore()
        initRecyclerView()

        mQuery.let {
            mDeveloperListAdapter?.setQuery(query = mQuery as Query)
        }
    }

     override fun onStart() {
        super.onStart()
        // Start listening for Firestore updates
        mDeveloperListAdapter?.let {
            mDeveloperListAdapter?.startListening()
        }
    }

     override fun onStop() {
        super.onStop()
        mDeveloperListAdapter.let {
            mDeveloperListAdapter?.stopListening()
        }
    }



    private fun initFirestore() {
        mFirestore = FirebaseFirestore.getInstance()

        val specificDateTime = Calendar.getInstance()
        specificDateTime.add(Calendar.DAY_OF_YEAR, -9)

        mQuery = mFirestore?.collection("developers")
                ?.orderBy("date", Direction.DESCENDING)
                ?.orderBy("name", Direction.DESCENDING)
                ?.whereGreaterThan("date", specificDateTime.time)

    }

    private fun initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView")
        }

        mQuery?.let {

            mDeveloperListAdapter =   object : DevelopersRVAdapter(mQuery as Query) {

                override fun onDataChanged() {
                    // Show/hide content if the query returns empty.
                    if (itemCount == 0) {
                        this@MainActivity.mRVDevelopers.visibility = View.GONE
                    } else {
                        this@MainActivity.mRVDevelopers.visibility = View.VISIBLE
                    }
                }

                override fun onError(e: FirebaseFirestoreException) {
                    // TODO: Show a snackbar on errors
                }
            }


            mRVDevelopers.layoutManager = LinearLayoutManager(this)
            mRVDevelopers.adapter = mDeveloperListAdapter


        }
    }
}
