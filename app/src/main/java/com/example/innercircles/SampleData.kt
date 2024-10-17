package com.example.innercircles

import com.example.innercircles.api.data.Post
import com.example.innercircles.api.data.PostResponse
import com.google.gson.Gson

object SampleData {

    val jsonResponse = """
{
    "data": [
        {
            "id": "1",
            "type": "post",
            "attributes": {
                "id": 1,
                "author_id": 1,
                "caption": "This is a caption",
                "created_at": "2024-07-20T00:34:51.898Z",
                "updated_at": "2024-07-20T00:34:51.898Z",
                "author_display_name": "KT",
                "circle_name": "High school friends",
                "contents": {
                    "data": []
                },
                "comments": {
                    "data": [
                        {
                            "id": "1",
                            "type": "comment",
                            "attributes": {
                                "id": 1,
                                "author_id": 1,
                                "parent_comment_id": null,
                                "post_id": 1,
                                "comment_text": "great picture",
                                "created_at": "2024-08-10T20:04:17.534Z",
                                "updated_at": "2024-08-10T20:04:17.534Z",
                                "author_display_name": "KT"
                            }
                        },
                        {
                            "id": "2",
                            "type": "comment",
                            "attributes": {
                                "id": 2,
                                "author_id": 3,
                                "parent_comment_id": 1,
                                "post_id": 1,
                                "comment_text": "I agree",
                                "created_at": "2024-08-10T20:42:58.186Z",
                                "updated_at": "2024-08-10T20:42:58.186Z",
                                "author_display_name": "chagurlll"
                            }
                        }
                    ]
                }
            }
        },
        {
            "id": "39",
            "type": "post",
            "attributes": {
                "id": 39,
                "author_id": 1,
                "caption": "7/28",
                "created_at": "2024-07-28T18:16:48.258Z",
                "updated_at": "2024-07-28T18:16:48.258Z",
                "author_display_name": "Maxwell",
                "circle_name": "High school friends",
                "contents": {
                    "data": [
                        {
                            "id": "11",
                            "type": "content",
                            "attributes": {
                                "id": 11,
                                "video_url": null,
                                "presigned_image_url": "https://inner-circles-app.s3.amazonaws.com/1_39_images/5109f0394de575d0b9ac.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA6ESHSQUIBSVWTSGG%2F20240827%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20240827T014345Z&X-Amz-Expires=3600&X-Amz-SignedHeaders=host&X-Amz-Signature=570c732348669a3842fc1e1304545722135aea414a56d8755599b8d2c19f40ee"
                            }
                        }
                    ]
                },
                "comments": {
                    "data": [
                        {
                            "id": "6",
                            "type": "comment",
                            "attributes": {
                                "id": 6,
                                "author_id": 1,
                                "parent_comment_id": null,
                                "post_id": 39,
                                "comment_text": "beautiful shot",
                                "created_at": "2024-08-23T17:40:52.444Z",
                                "updated_at": "2024-08-23T17:40:52.444Z",
                                "author_display_name": "KT"
                            }
                        },
                        {
                            "id": "7",
                            "type": "comment",
                            "attributes": {
                                "id": 7,
                                "author_id": 1,
                                "parent_comment_id": 1,
                                "post_id": 39,
                                "comment_text": "tght",
                                "created_at": "2024-08-24T17:05:58.194Z",
                                "updated_at": "2024-08-24T17:05:58.194Z",
                                "author_display_name": "KT"
                            }
                        },
                        {
                            "id": "8",
                            "type": "comment",
                            "attributes": {
                                "id": 8,
                                "author_id": 1,
                                "parent_comment_id": 1,
                                "post_id": 39,
                                "comment_text": "tght",
                                "created_at": "2024-08-24T17:06:29.173Z",
                                "updated_at": "2024-08-24T17:06:29.173Z",
                                "author_display_name": "KT"
                            }
                        },
                        {
                            "id": "9",
                            "type": "comment",
                            "attributes": {
                                "id": 9,
                                "author_id": 1,
                                "parent_comment_id": 1,
                                "post_id": 39,
                                "comment_text": "tght",
                                "created_at": "2024-08-24T17:06:57.094Z",
                                "updated_at": "2024-08-24T17:06:57.094Z",
                                "author_display_name": "KT"
                            }
                        },
                        {
                            "id": "10",
                            "type": "comment",
                            "attributes": {
                                "id": 10,
                                "author_id": 1,
                                "parent_comment_id": 1,
                                "post_id": 39,
                                "comment_text": "new new",
                                "created_at": "2024-08-24T17:07:13.418Z",
                                "updated_at": "2024-08-24T17:07:13.418Z",
                                "author_display_name": "KT"
                            }
                        }
                    ]
                }
            }
        },
        {
            "id": "47",
            "type": "post",
            "attributes": {
                "id": 47,
                "author_id": 1,
                "caption": "Beach Views 😎",
                "created_at": "2024-08-06T02:57:33.045Z",
                "updated_at": "2024-08-06T02:57:33.045Z",
                "author_display_name": "Pam the Punisher",
                "circle_name": "College friends",
                "contents": {
                    "data": [
                        {
                            "id": "16",
                            "type": "content",
                            "attributes": {
                                "id": 16,
                                "video_url": null,
                                "presigned_image_url": "https://inner-circles-app.s3.amazonaws.com/1_47_images/c2607f1082614c267c06.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA6ESHSQUIBSVWTSGG%2F20240827%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20240827T014346Z&X-Amz-Expires=3600&X-Amz-SignedHeaders=host&X-Amz-Signature=6a3e83e241f2f51d8526d654794d64577fd3a5b1fe4432cc124cab91fdca8e55"
                            }
                        }
                    ]
                },
                "comments": {
                    "data": []
                }
            }
        }
    ]
}
"""

    val gson = Gson()
    val postResponse: PostResponse = gson.fromJson(jsonResponse, PostResponse::class.java)

    private val samplePosts: List<Post> = gson.fromJson(jsonResponse, PostResponse::class.java).data

    fun returnSamplePosts(): List<Post> {
        return samplePosts
    }
}