package com.rysanek.diary.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rysanek.diary.data.models.Diary
import com.rysanek.diary.data.models.Mood
import com.rysanek.diary.ui.theme.Elevation
import com.rysanek.diary.utils.toInstant
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@Composable
fun DiaryHolder(
    diary: Diary,
    onClick: (String) -> Unit
) {
    
    val localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }
    
    Row(modifier = Modifier
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick(diary._id.toString()) }
    ) {
        
        Spacer(modifier = Modifier.width(14.dp))
        
        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.level1
        ) {
        
        }
        
        Spacer(modifier = Modifier.width(20.dp))
        
        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) { it.size.height.toDp() }
                },
            tonalElevation = Elevation.level1
        ) {
            
            Column(modifier = Modifier.fillMaxWidth()) {
                
                DiaryHeader(moodName = diary.mood, time = diary.date.toInstant())
                
                Text(
                    modifier = Modifier.padding(all = 14.dp),
                    text = diary.description,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                
                
            }
            
        }
    }
    
}

@Composable
fun DiaryHeader(moodName: String, time: Instant) {
    val mood by remember { mutableStateOf(Mood.valueOf(moodName)) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(mood.containerColor)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = mood.icon),
                contentDescription = "Mood Icon",
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = mood.name,
                color = mood.contentColor,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )
        }
        Text(
            text = SimpleDateFormat("hh:mm a", Locale.US)
                .format(Date.from(time)),
            color = mood.contentColor,
            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DiaryHolderPreview() {
    DiaryHolder(
        diary = Diary().apply {
            title = "Diary 1"
            description = "Lorem Ipsum dolor sit amet, consecteur adipiscing elit, sed do eiusmod temp"
            mood = Mood.Happy.name
        },
        onClick = {}
    )
}