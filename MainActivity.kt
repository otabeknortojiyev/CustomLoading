package uz.yayra.otabek.equilibrium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.yayra.otabek.equilibrium.ui.theme.EquilibriumTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      EquilibriumTheme {
        CustomLoading()
      }
    }
  }
}

@Composable
fun CustomLoading() {
  var startFirstAnimation = remember { mutableStateOf(false) }
  var startSecondAnimation = remember { mutableStateOf(false) }
  var isThirdCircleVisible = remember { mutableStateOf(false) }

  val mainCircleSize: Dp = 80.dp
  val secondCircleSize: Dp = mainCircleSize / 2
  val thirdCircleSize: Dp = secondCircleSize / 2

  val firstOffset = animateDpAsState(
    targetValue = if (startFirstAnimation.value) mainCircleSize else 0.dp,
    animationSpec = tween(durationMillis = 800),
    label = "FirstOffsetAnimation"
  )
  val firstSize = animateDpAsState(
    targetValue = if (startFirstAnimation.value) mainCircleSize / 2 else mainCircleSize,
    animationSpec = tween(durationMillis = 800),
    label = "FirstSizeAnimation"
  )
  val secondOffset = animateDpAsState(
    targetValue = if (startSecondAnimation.value) mainCircleSize + secondCircleSize + secondCircleSize / 4 else mainCircleSize,
    animationSpec = tween(durationMillis = 800),
    label = "SecondOffsetAnimation"
  )
  val secondSize = animateDpAsState(
    targetValue = if (startSecondAnimation.value) thirdCircleSize else secondCircleSize,
    animationSpec = tween(durationMillis = 800),
    label = "SecondSizeAnimation"
  )

  LaunchedEffect(firstOffset.value) {
    snapshotFlow { firstOffset.value }.collect { firstOffset ->
      if (firstOffset == mainCircleSize) {
        isThirdCircleVisible.value = true
        startSecondAnimation.value = true
      }
    }
  }

  LaunchedEffect(key1 = secondOffset.value) {
    snapshotFlow { secondOffset.value }.collect { secondOffset ->
      if (secondOffset == mainCircleSize + secondCircleSize + secondCircleSize / 4) {
        startSecondAnimation.value = false
      }
      if (secondOffset == mainCircleSize && !startSecondAnimation.value) {
        isThirdCircleVisible.value = false
        startFirstAnimation.value = false
      }
    }
  }

  LaunchedEffect(key1 = firstOffset.value) {
    snapshotFlow { firstOffset.value }.collect { firstOffset ->
      if (firstOffset == 0.dp) {
        startFirstAnimation.value = true
      }
    }
  }

  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(100.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(contentAlignment = Alignment.Center) {
        //Second left circle
        Box(
          modifier = Modifier
            .offset(x = -secondOffset.value)
            .size(secondSize.value)
            .background(if (isThirdCircleVisible.value) Color.Green else Color.Transparent, CircleShape)
        )

        //Second right circle
        Box(
          modifier = Modifier
            .offset(x = secondOffset.value)
            .size(secondSize.value)
            .background(if (isThirdCircleVisible.value) Color.Green else Color.Transparent, CircleShape)
        )

        //First left circle
        Box(
          modifier = Modifier
            .offset(x = -firstOffset.value)
            .size(firstSize.value)
            .background(Color.Green, CircleShape)
        )
        //First right circle
        Box(
          modifier = Modifier
            .offset(x = firstOffset.value)
            .size(firstSize.value)
            .background(Color.Green, CircleShape)
        )

        //Main Circle
        Box(
          modifier = Modifier
            .size(mainCircleSize)
            .background(Color.Green, CircleShape)
        )
      }
    }
  }
}
