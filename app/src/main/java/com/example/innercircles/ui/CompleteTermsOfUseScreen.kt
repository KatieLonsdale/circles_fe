package com.example.innercircles.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.innercircles.R

@Composable
fun CompleteTermsOfUseScreen(
    onClickBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 2.dp)
    ){
        // Box for back arrow
        IconButton(
            onClick = {
                onClickBack()
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White
            ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back), // Use your back arrow drawable
                contentDescription = "Back",
                modifier = Modifier.align(Alignment.Start),
            )
        }
        Text(
            text = "Terms of Use",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "Last Updated: 12/18/2024",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "Welcome to ChatterBox! These Terms of Use (\"Terms\") govern your access to and use of our app, website, and services (collectively, the \"Services\"). By using the Services, you agree to be bound by these Terms. If you do not agree, do not use the Services.",
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "1. Acceptance of Terms",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "By creating an account, accessing, or using the Services, you confirm that you have read, understood, and agree to these Terms, as well as our Privacy Policy. If you are under the age of 18, you must have the consent of a parent or guardian to use the Services.",
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "2. User-Generated Content (UGC)",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "2.1 Definition: UGC includes any content you create, upload, share, or otherwise make available through the Services, including but not limited to posts, comments, images, videos, links, and other materials.\n" +
                    "2.2 Prohibited Content: You may not create, upload, or share content that:\n" +
                    "Violates any applicable laws or regulations.\n" +
                    "Is harmful, abusive, threatening, harassing, defamatory, or discriminatory.\n" +
                    "Promotes violence, self-harm, or illegal activities.\n" +
                    "Contains explicit or sexually suggestive material.\n" +
                    "Violates the intellectual property rights or privacy of others.\n" +
                    "Includes spam, phishing, or malicious content.\n" +
                    "2.3 Responsibility for UGC: You are solely responsible for any UGC you create or share. By uploading content, you grant us a worldwide, non-exclusive, royalty-free license to use, display, and distribute your content in connection with the Services.",
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "3. Account Responsibilities",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "3.1 Account Creation: To use certain features, you must create an account. You agree to provide accurate and complete information and to keep your account information updated.\n" +
                    "3.2 Security: You are responsible for maintaining the confidentiality of your account credentials and for all activities that occur under your account. Notify us immediately if you suspect unauthorized access.\n",
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "4. Moderation and Reporting",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "4.1 Moderation: We reserve the right to review, remove, or disable access to any content that violates these Terms or is otherwise objectionable.\n" +
                    "4.2 Reporting: If you encounter content or behavior that violates these Terms, please use the in-app reporting feature or contact us at chatterboxsocialapp@gmail.com.",
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "5. Termination",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "We may suspend or terminate your access to the Services at any time, with or without notice, if you violate these Terms or engage in activities harmful to the community or the Services.",
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "6. Intellectual Property",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "6.1 Ownership: The Services and all related materials, including logos, designs, and code, are owned by Katie Lonsdale or our licensors and are protected by intellectual property laws.\n" +
                    "6.2 Restrictions: You may not copy, modify, distribute, sell, or lease any part of the Services unless authorized by us in writing.",
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "7. Disclaimer and Limitation of Liability",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "7.1 Disclaimer: The Services are provided \"as is\" and \"as available.\" We do not guarantee uninterrupted or error-free use of the Services.\n" +
                    "7.2 Limitation of Liability: To the maximum extent permitted by law, we are not liable for any indirect, incidental, or consequential damages arising from your use of the Services.",
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "8. Changes to Terms",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "We may update these Terms from time to time. We will notify you of material changes by posting the updated Terms in the app. Continued use of the Services after changes means you accept the updated Terms.",
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "9. Governing Law",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "These Terms are governed by and construed in accordance with the laws of the State of Colorado, United States. Any disputes will be resolved in the courts located in Colorado, USA.",
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "10. Contact Us",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "If you have any questions or concerns about these Terms, please contact us at:\n" +
                    " chatterboxsocialapp@gmail.com",
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
        Text(
            text = "By using ChatterBox, you acknowledge that you have read, understood, and agree to these Terms of Use.",
            modifier = Modifier.padding(
                bottom = 16.dp,
                start = 10.dp,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CompleteTermsOfUseScreenPreview() {
    CompleteTermsOfUseScreen(
        onClickBack = {},
    )
}