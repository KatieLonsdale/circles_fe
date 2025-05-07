package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katielonsdale.chatterbox.R
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.api.data.Comment

@Composable
fun CommentCard(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        // Combined author name and comment text
        Row(
            modifier = Modifier.weight(0.9f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Author name
            Text(
                text = comment.attributes.authorDisplayName,
                color = Color.DarkGray,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )
            
            // Comment text
            SelectionContainer {
                Text(
                    text = comment.attributes.commentText,
                    color = Color.DarkGray,
                    fontSize = 20.sp,
                )
            }
        }
        
        // Reply icon at the end
        Icon(
            painter = painterResource(id = R.drawable.ic_reply),
            contentDescription = "Reply",
            tint = Color.DarkGray,
            modifier = Modifier
                .minimumInteractiveComponentSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommentCard() {
    val posts = SampleData.returnSamplePosts()
    val comments = posts[0].attributes.comments.data
    CommentCard(comment = comments[0])
}