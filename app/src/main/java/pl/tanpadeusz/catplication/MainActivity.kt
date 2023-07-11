package pl.tanpadeusz.catplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import pl.tanpadeusz.catplication.ui.theme.CatplicationTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val catplicationViewModel: CatplicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    this.CatLayout()
                }
            }
        }
    }

    private fun yayButtonClicked() {
        Timber.d("yay button clicked")
        this.catplicationViewModel.postYayVote()
    }

    private fun nayButtonClicked() {
        Timber.d("nay button clicked")
        this.catplicationViewModel.postNayVote()
    }

    private fun nextButtonClicked() {
        Timber.d("next button clicked")
        this.catplicationViewModel.getImage()
    }

    private fun showToast(message: String) {
        Timber.d("showing toast")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    private fun CatLayout() {
        val resources = this.resources
        var model: String by remember {
            mutableStateOf("")
        }

        this.catplicationViewModel.imageResponse.observe(this) { response ->
            model = response?.url ?: ""
        }

        this.catplicationViewModel.voteResponse.observe(this) { response ->
            if (response == null)
                this@MainActivity.showToast(resources.getString(R.string.null_response_text))
            else
            {
                val message = resources.getString(when(response.value) {
                    -1 -> R.string.voted_nay_text
                    1 -> R.string.voted_yay_text
                    else -> R.string.voted_unknown_text
                })
                this.showToast(message)
                this.catplicationViewModel.getImage()
            }
        }

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (image, yayButton, nextButton, nayButton) = createRefs()
            val guideline = createGuidelineFromTop(0.85F)

            val margin = 16.dp
            val halfMargin = margin / 2

            GlideImage(
                model = model,
                contentDescription = resources.getString(R.string.image_content_description),
                modifier = Modifier.constrainAs(image) {
                    this.start.linkTo(parent.start, margin)
                    this.end.linkTo(parent.end, margin)
                    this.top.linkTo(parent.top, margin)
                    this.bottom.linkTo(guideline, halfMargin)
                    this.height = Dimension.fillToConstraints
                    this.width = Dimension.fillToConstraints
                }
            )

            Button(
                onClick = this@MainActivity::yayButtonClicked,
                modifier = Modifier.constrainAs(yayButton) {
                    this.start.linkTo(parent.start, margin)
                    this.end.linkTo(nextButton.start, halfMargin)
                    this.top.linkTo(guideline, halfMargin)
                    this.bottom.linkTo(parent.bottom, margin)
                    this.height = Dimension.fillToConstraints
                    this.width = Dimension.fillToConstraints
                }
            ) {
                Text(text = resources.getString(R.string.yay_button_text))
            }

            Button(
                onClick = this@MainActivity::nextButtonClicked,
                modifier = Modifier.constrainAs(nextButton) {
                    this.start.linkTo(yayButton.end, halfMargin)
                    this.end.linkTo(nayButton.start, halfMargin)
                    this.top.linkTo(guideline, halfMargin)
                    this.bottom.linkTo(parent.bottom, margin)
                    this.height = Dimension.fillToConstraints
                    this.width = Dimension.fillToConstraints
                }
            ) {
                Text(text = resources.getString(R.string.next_button_text))
            }

            Button(
                onClick = this@MainActivity::nayButtonClicked,
                modifier = Modifier.constrainAs(nayButton) {
                    this.start.linkTo(nextButton.end, halfMargin)
                    this.end.linkTo(parent.end, margin)
                    this.top.linkTo(guideline, halfMargin)
                    this.bottom.linkTo(parent.bottom, margin)
                    this.height = Dimension.fillToConstraints
                    this.width = Dimension.fillToConstraints
                }
            ) {
                Text(text = resources.getString(R.string.nay_button_text))
            }
        }

        this.catplicationViewModel.getImage()
    }

    @Preview(showBackground = true)
    @Composable
    private fun CatLayoutPreview() {
        CatplicationTheme {
            CatLayout()
        }
    }
}