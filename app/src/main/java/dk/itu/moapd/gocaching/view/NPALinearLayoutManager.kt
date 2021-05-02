package dk.itu.moapd.gocaching.view

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class NPALinearLayoutManager(context: Context?) : LinearLayoutManager(context) {
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}