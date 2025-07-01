package com.katielonsdale.chatterbox

import com.katielonsdale.chatterbox.api.data.Post
import com.katielonsdale.chatterbox.api.data.PostsResponse
import com.google.gson.Gson
import com.katielonsdale.chatterbox.api.data.Circle
import com.katielonsdale.chatterbox.api.data.CircleAttributes
import com.katielonsdale.chatterbox.api.data.Notification
import com.katielonsdale.chatterbox.api.data.NotificationAttributes

object SampleData {

    private val jsonResponse = """
{
    "data": [
        {
            "id": "1",
            "type": "post",
            "attributes": {
                "id": 1,
                "author_id": 1,
                "caption": "This is a caption. It is very long. These captions should look nicer when they are really long.",
                "created_at": "2024-07-20T00:34:51.898Z",
                "updated_at": "2024-07-20T00:34:51.898Z",
                "author_display_name": "KT",
                "circle_id": 1,
                "circle_name": "High school friends",
                "contents": {
                    "data": [
                    {
                        "id": "1",
                        "type": "content",
                        "attributes": {
                            "id": 1,
                            "presigned_image_url": "https://images.unsplash.com/photo-1749587452499-ea1fd591e63f?q=80&w=1056&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                        }
                    }
                    ]
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
                                "author_display_name": "KT",
                                "replies": {
                                    "data": [
                                        {
                                            "id": "7",
                                            "attributes": {
                                                "id": 7,
                                                "author_id": 1,
                                                "parent_comment_id": 1,
                                                "post_id": 1,
                                                "comment_text": "I agree",
                                                "created_at": "2024-08-10T20:39:58.186Z",
                                                "updated_at": "2024-08-10T20:39:58.186Z",
                                                "author_display_name": "maxwell",
                                                "replies": {
                                                    "data": [
                                                        {
                                                            "id": "10",
                                                            "attributes": {
                                                                "id": 10,
                                                                "author_id": 3,
                                                                "parent_comment_id": 7,
                                                                "post_id": 1,
                                                                "comment_text": "Here is a very long comment that should wrap properly and not cover the reply button",
                                                                "created_at": "2024-08-10T21:52:58.186Z",
                                                                "updated_at": "2024-08-10T21:52:58.186Z",
                                                                "author_display_name": "chagurlll",
                                                                "replies": {"data": []}
                                                            }
                                                        }
                                                    ]
                                                }
                                            }
                                        },
                                        {
                                            "id": "2",
                                            "attributes": {
                                                "id": 2,
                                                "author_id": 3,
                                                "parent_comment_id": 1,
                                                "post_id": 1,
                                                "comment_text": "I disagree",
                                                "created_at": "2024-08-10T20:52:58.186Z",
                                                "updated_at": "2024-08-10T20:52:58.186Z",
                                                "author_display_name": "chagurlll",
                                                "replies": {"data": []}
                                            }
                                        }
                                    ]
                                }
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
                                "author_display_name": "chagurlll",
                                "replies": {"data": []}
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
                                "author_display_name": "KT",
                                "replies": {"data": []}
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
                                "author_display_name": "KT",
                                "replies": {"data": []}
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
                                "author_display_name": "KT",
                                "replies": {"data": []}
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
                                "author_display_name": "KT",
                                "replies": {"data": []}
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
                                "author_display_name": "KT",
                                "replies": {"data": []}
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
    val postsResponse: PostsResponse = gson.fromJson(jsonResponse, PostsResponse::class.java)

    private val samplePosts: List<Post> = gson.fromJson(jsonResponse, PostsResponse::class.java).data

    fun returnSamplePosts(): List<Post> {
        return samplePosts
    }

    val notifications = listOf(
        Notification(
            id = "123",
            type = "notification",
            attributes = NotificationAttributes(
                id = "123",
                message = "Max commented: what a great idea!",
                read = false,
                action = "comment_created",
                createdAt = "2025-03-27 19:20:43.598264",
                updatedAt = "2025-03-27 19:20:43.598264",
                circleId = "1",
                circleName = "Circle Name",
                notifiableType = "comment",
                notifiableId = "13",
                postId = "1"
            )
        ),
        Notification(
            id = "124",
            type = "notification",
            attributes = NotificationAttributes(
                id = "124",
                message = "Lena replied: Totally agree with you!",
                read = false,
                action = "reply_created",
                createdAt = "2025-04-01 10:05:12.123456",
                updatedAt = "2025-04-01 10:05:12.123456",
                circleId = "1",
                circleName = "Circle Name",
                notifiableType = "comment",
                notifiableId = "14",
                postId = "1"
            )
        ),
        Notification(
            id = "125",
            type = "notification",
            attributes = NotificationAttributes(
                id = "125",
                message = "Your post was liked by Alex",
                read = false,
                action = "like_created",
                createdAt = "2025-04-03 08:30:00.000000",
                updatedAt = "2025-04-03 08:30:00.000000",
                circleId = "1",
                circleName = "Circle Name",
                notifiableType = "like",
                notifiableId = "15",
                postId = "1"
            )
        ),
        Notification(
            id = "126",
            type = "notification",
            attributes = NotificationAttributes(
                id = "126",
                message = "Nora mentioned you in a comment",
                read = true,
                action = "mention_created",
                createdAt = "2025-04-05 15:45:22.987654",
                updatedAt = "2025-04-05 15:45:22.987654",
                circleId = "1",
                circleName = "Circle Name",
                notifiableType = "mention",
                notifiableId = "16",
                postId = "1"
            )
        ),
        Notification(
            id = "127",
            type = "notification",
            attributes = NotificationAttributes(
                id = "127",
                message = "Event Reminder: Dinner tonight at 6:00 PM with Circle Name",
                read = true,
                action = "event_reminder",
                createdAt = "2025-04-10 09:00:00.000000",
                updatedAt = "2025-04-10 09:00:00.000000",
                circleId = "1",
                circleName = "Circle Name",
                notifiableType = "event",
                notifiableId = "17",
                postId = null
            )
        ),
        Notification(
            id = "128",
            type = "notification",
            attributes = NotificationAttributes(
                id = "128",
                message = "A new member, Sarah, joined your circle!",
                read = true,
                action = "member_joined",
                createdAt = "2025-04-12 13:22:10.654321",
                updatedAt = "2025-04-12 13:22:10.654321",
                circleId = "1",
                circleName = "Circle Name",
                notifiableType = "user",
                notifiableId = "18",
                postId = null
            )
        )
    )

    val returnSampleNotifications: List<Notification> = notifications


    val chatters = listOf(
        Circle(
            id = "1",
            type = "circle",
            attributes = CircleAttributes(
                id = 1,
                userId = "1",
                name = "High School Friends",
                description = "ole pals from RHS class of 2008"
            )
        ),
    Circle(
            id = "2",
            type = "circle",
            attributes = CircleAttributes(
                id = 2,
                userId = "1",
                name = "The Fam",
                description = "Lonsdale/Forbes family chat"
            )
        ),
    Circle(
            id = "3",
            type = "circle",
            attributes = CircleAttributes(
                id = 3,
                userId = "1",
                name = "College Roomies",
                description = "125 Husky Lane 4-ever <3"
            )
        ),
    Circle(
            id = "4",
            type = "circle",
            attributes = CircleAttributes(
                id = 4,
                userId = "1",
                name = "Work budz",
                description = "planning happy hours & yap"
            )
        ),
    Circle(
            id = "5",
            type = "circle",
            attributes = CircleAttributes(
                id = 5,
                userId = "1",
                name = "Willowleaf Neighbors",
                description = "Chat to organize block parties, can drives, and communicate any issues! Invite any neighbors who want to join."
            )
        ),
    Circle(
            id = "6",
            type = "circle",
            attributes = CircleAttributes(
                id = 6,
                userId = "1",
                name = "Tues Night Run Club",
                description = "We meet Tuesdays at 5pm at Clement Park for a 5 mile jog followed by drinks at Lakehouse!"
            )
        ),
    )

    val returnSampleChatters: List<Circle> = chatters


}