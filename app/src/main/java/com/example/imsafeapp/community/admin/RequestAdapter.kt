package com.example.imsafeapp.community.admin

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imsafeapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RequestAdapter(private val requests: List<Request>) : RecyclerView.Adapter<RequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]
        holder.userIdText.text = request.userName

        holder.approveButton.setOnClickListener {
            val userId = requests[position].userId ?: ""
            val constituency = request.constituency // Access constituency directly from the request object
            val userName = request.userName
            val phoneNumber = request.phoneNumber
            Log.d("QWE", "approve btn clicked $userId")
            updateRequestApprovalStatus(userId, constituency!!, userName!!, phoneNumber!!)
        }
        holder.btnCancal.setOnClickListener {
            val userId = requests[position].userId ?: ""
            deleteRequest(userId)
        }
    }

    override fun getItemCount() = requests.size

    private fun updateRequestApprovalStatus(userId: String, constituency: String, userName: String, phoneNumber: String) {
        val database = FirebaseDatabase.getInstance()
        val requestRef: DatabaseReference = database.getReference("Requests").child(userId)

        // Create a new map with all fields, including approved set to true
        val updatedRequest = hashMapOf(
            "approved" to true,
            "constituency" to constituency,
            "userName" to userName,
            "phoneNumber" to phoneNumber,
            "userId" to userId
        )

        // Update the entire object under the userId node
        requestRef.setValue(updatedRequest)
            .addOnSuccessListener {
                println("Approval status updated successfully for userId: $userId")
            }
            .addOnFailureListener { exception ->
                println("Error updating approval status for userId: $userId: $exception")
            }
    }

    private fun deleteRequest(userId: String) {
        val database = FirebaseDatabase.getInstance()
        val requestRef: DatabaseReference = database.getReference("Requests").child(userId)

        requestRef.removeValue()
            .addOnSuccessListener {
                println("Request deleted successfully for userId: $userId")
            }
            .addOnFailureListener { exception ->
                println("Error deleting request for userId: $userId: $exception")
            }
    }

}
