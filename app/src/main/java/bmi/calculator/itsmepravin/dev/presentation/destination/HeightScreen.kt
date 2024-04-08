package bmi.calculator.itsmepravin.dev.presentation.destination

import android.content.res.Configuration
import android.media.AudioManager
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import bmi.calculator.itsmepravin.dev.R
import bmi.calculator.itsmepravin.dev.domain.AppConstants
import bmi.calculator.itsmepravin.dev.presentation.MainViewModel
import bmi.calculator.itsmepravin.dev.presentation.components.Button
import bmi.calculator.itsmepravin.dev.presentation.components.Scale
import bmi.calculator.itsmepravin.dev.presentation.destination.destinations.ResultScreenDestination
import bmi.calculator.itsmepravin.dev.presentation.ui.theme.BMICalculatorTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun HeightScreen(
    navigator: DestinationsNavigator,
    viewModel: MainViewModel,
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    val audioManager = remember {
        ContextCompat.getSystemService(context, AudioManager::class.java)
    }

    LaunchedEffect(viewModel.heightInCm) {
        audioManager?.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD, 1f)
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    HeightScreenContent(
        currentHeight = viewModel.heightInCm,
        onHeightChange = viewModel::updateHeight,
        onContinue = {
            navigator.navigate(ResultScreenDestination)
        }
    )
}

@Composable
fun HeightScreenContent(
    currentHeight: Int,
    onHeightChange: (Int) -> Unit,
    onContinue: () -> Unit = {}
) {
    val screenOrientation = LocalConfiguration.current.orientation

    Surface(modifier = Modifier.fillMaxSize()) {
        val continueButton = movableContentOf {
            Button(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp),
                onClick = onContinue
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.CenterHorizontally
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.btn_continue),
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null
                    )
                }
            }
        }
        val informationInterface = movableContentOf {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(36.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.height_screen_headline),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.CenterVertically
                    ),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 4.dp,
                            alignment = Alignment.CenterHorizontally
                        ),
                    ) {
                        Text(
                            modifier = Modifier.alignByBaseline(),
                            text = currentHeight.toString(),
                            style = MaterialTheme.typography.displayMedium
                        )
                        Text(
                            modifier = Modifier.alignByBaseline(),
                            color = MaterialTheme.colorScheme.outline,
                            text = stringResource(R.string.height_unit),
                        )
                    }
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.outline
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                space = 4.dp,
                                alignment = Alignment.CenterHorizontally
                            ),
                        ) {
                            val heightInFoot = currentHeight / 30.48
                            Text(
                                modifier = Modifier.alignByBaseline(),
                                text = AppConstants.DECIMAL_FORMAT.format(heightInFoot)
                            )
                            Text(
                                modifier = Modifier.alignByBaseline(),
                                text = stringResource(R.string.height_unit_alt)
                            )
                        }
                    }
                }

                if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    continueButton()
                }
            }
        }
        val weightScale = @Composable {
            val orientation = when (screenOrientation) {
                Configuration.ORIENTATION_LANDSCAPE -> Orientation.Vertical
                else -> Orientation.Horizontal
            }
            Scale(
                modifier = Modifier.fillMaxSize(),
                minValue = 40,
                maxValue = 220,
                orientation = orientation,
                currentValue = currentHeight,
                onValueChanged = onHeightChange,
                horizontalAlignment = Alignment.CenterHorizontally,
            )
        }

        when (screenOrientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1.5f)
                            .fillMaxHeight()
                    ) {
                        informationInterface()
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        weightScale()
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                        .padding(bottom = 36.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        informationInterface()
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        weightScale()
                    }
                    continueButton()
                }
            }
        }
    }
}

@Preview
@Composable
fun HeightScreenPreview() {
    BMICalculatorTheme {
        HeightScreenContent(
            currentHeight = AppConstants.INITIAL_HEIGHT,
            onHeightChange = {}
        )
    }
}