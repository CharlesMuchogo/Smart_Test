package com.charlesmuchogo.research.presentation.common

import android.text.Html
import android.text.TextUtils
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun HtmlText(modifier: Modifier = Modifier, htmlContent: String, maxLines: Int = Int.MAX_VALUE) {
    val textColor = MaterialTheme.colorScheme.onBackground
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                text = Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_LEGACY)
                setMaxLines(maxLines)
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(textColor.toArgb())
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}