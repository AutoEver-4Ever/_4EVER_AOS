package org.ever.everp.aos.ui.redirect

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle

/**
 * OAuth2 리다이렉트를 수신하는 투명 액티비티.
 * 앞으로 AppAuth/레포지토리로 전달하는 핸들링을 연결한다.
 */
class RedirectReceiverActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data: Uri? = intent?.data
        // TODO: AppAuth AuthorizationResponse.fromIntent(intent) 등을 이용해 처리 연결

        // 현재는 단순히 런치 액티비티로 복귀
        startActivity(Intent(this, Class.forName("org.ever.everp.aos.ui.login.LoginActivity")).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            data?.let { setData(it) }
        })
        finish()
    }
}

