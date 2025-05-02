package com.katielonsdale.chatterbox.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.katielonsdale.chatterbox.SampleData
import com.katielonsdale.chatterbox.api.data.Comment

@Composable
fun CommentCard(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Text(
            text = comment.attributes.authorDisplayName,
            color = Color.DarkGray,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(10.dp))

        SelectionContainer {
            Text(
//                text = AnnotatedString.rememberAutoLinkText(comment.attributes.commentText),
                text = comment.attributes.commentText,
                color = Color.DarkGray,
                fontSize = 15.sp,
            )
        }
    }

    if(comment.attributes.)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ){

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCommentCard() {
    val posts = SampleData.returnSamplePosts()
    val comments = posts[0].attributes.comments.data
    CommentCard(comment = comments[0])
}