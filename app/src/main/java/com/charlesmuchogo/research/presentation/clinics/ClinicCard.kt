
package com.charlesmuchogo.research.presentation.clinics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.charlesmuchogo.research.domain.models.Clinic
import com.charlesmuchogo.research.presentation.utils.openDialer

@Composable
fun ClinicCard(modifier: Modifier = Modifier, clinic: Clinic) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(vertical = 4.dp)
            .shadow(1.dp, shape = RoundedCornerShape(5))
            .clip(
                RoundedCornerShape(5)
            )
            .background(MaterialTheme.colorScheme.background)
            .clickable {

            }
            .testTag(clinic.id.toString())
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    clinic.name,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleMedium,
                )

            }

            Spacer(modifier = Modifier.weight(1f))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.secondaryContainer
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp),
                        contentDescription = null
                    )
                    Text(
                        clinic.address.ifBlank { "N/A" },
                        fontWeight = FontWeight.Light,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(role = Role.Button, onClick = {
                            openDialer(
                                phoneNumber = clinic.contacts,
                                context = context
                            )
                        })
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(20.dp),
                            contentDescription = null
                        )
                        Text(
                            clinic.contacts.ifBlank { "N/A" },
                            fontWeight = FontWeight.Light,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
            }
        }
    }
}
