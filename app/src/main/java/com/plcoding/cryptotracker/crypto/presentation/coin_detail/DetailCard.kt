package com.plcoding.cryptotracker.crypto.presentation.coin_detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.R
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CardDetail(title:String ,
               modifier: Modifier=Modifier,
               formattedText:String,
               icon:ImageVector,
               contentColor: Color = MaterialTheme.colorScheme.onSurface,
              ){

               val textStyle:TextStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = contentColor
               )

                Card (modifier = modifier
                        .padding(16.dp)
                        .shadow(
                        elevation = 15.dp,
                        shape = RectangleShape,
                        ambientColor = MaterialTheme.colorScheme.primary,
                        spotColor = MaterialTheme.colorScheme.primary
                ),
                shape = RectangleShape,
                border = BorderStroke(width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface),
                colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ){
                    AnimatedContent(targetState = icon ,
                        modifier = Modifier.align(
                            alignment = Alignment.CenterHorizontally
                        ),
                        label = "IconAnimation"
                    ) {
                        Icon(imageVector = it ,
                            contentDescription = title ,
                            tint = contentColor ,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(top = 10.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AnimatedContent(targetState = formattedText ,
                        modifier = Modifier.align(
                            alignment = Alignment.CenterHorizontally
                        ),
                        label = "formattedTextAnimation"
                    ) {
                        Text(text = it , style = textStyle ,
                            modifier = Modifier.padding(horizontal = 16.dp))
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(text = title , style = textStyle , fontSize = 14.sp,
                        modifier=Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 12.dp)
                            .padding(horizontal  = 16.dp))

                }

}

@PreviewLightDark
@Composable
fun PCard(){
    CryptoTrackerTheme { 
        CardDetail(
            title = "Market Cap",
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            formattedText = "$ 1565346643.64",
            icon = ImageVector.vectorResource(R.drawable.dollar)
        )
    }
}