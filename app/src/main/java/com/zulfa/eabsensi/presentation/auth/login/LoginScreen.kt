package com.zulfa.eabsensi.presentation.auth.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zulfa.eabsensi.R
import com.zulfa.eabsensi.core.data.Resource
import com.zulfa.eabsensi.presentation.theme.EAbsensiTheme
import com.zulfa.eabsensi.presentation.theme.Poppins
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit = {}
) {

    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    LaunchedEffect(loginState) {
        when (loginState) {
            is Resource.Success -> {
                Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }

            is Resource.Error -> {
                Toast.makeText(context, loginState.data?.message ?: "Login gagal", Toast.LENGTH_SHORT)
                    .show()
            }

            else -> { /* ignore Loading */
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                painter = painterResource(id = R.drawable.authpic),
                contentDescription = "Onboarding Illustration",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
            // Title
            Text(
                text = "Selamat Datang Di E-Absensi Peserta Magang",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle
            Text(
                text = "Dinas Ketahanan Pangan, Pertanian \ndan Perikanan Kota Banjarmasin",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "Silahkan masukan Username dan\n Password yang telah diberikan",
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                )
            )
            Spacer(modifier = Modifier.height(11.dp))

            OutlinedTextField(
                value = username,  // <-- use state
                onValueChange = { username = it }, // <-- update state
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = outlinedTextFieldColors( // Using the explicit import
                    focusedBorderColor = Color(0xFF0284C7),   // border when focused
                    unfocusedBorderColor = Color(0xFF0284C7), // border when not focused
                    cursorColor = Color(0xFF000000),          // cursor color
                    focusedLabelColor = Color(0xFF0284C7)     // label when focused
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.usn_icon), // your image in drawable
                        contentDescription = "User Icon",
                        tint = Color.Unspecified, // keep original colors of the image
                        modifier = Modifier
                            .size(29.dp)
                            .padding(start = 8.dp)
                    )
                }
            )
            OutlinedTextField(
                value = password,  // <-- use state
                onValueChange = { password = it }, // <-- update state
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF0284C7),
                    unfocusedBorderColor = Color(0xFF0284C7),
                    cursorColor = Color(0xFF000000),
                    focusedLabelColor = Color(0xFF0284C7)
                ),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.password_icon),
                        contentDescription = "Password Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(29.dp)
                            .padding(start = 8.dp)
                    )
                },
                trailingIcon = {
                    val image = if (passwordVisible)
                        painterResource(id = R.drawable.visibility_password_ic)
                    else
                        painterResource(id = R.drawable.visibility_off_password)

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = image,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            modifier = Modifier
                                .size(29.dp)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(11.dp))

            OutlinedButton(
                onClick = { if (username.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.login(username.trim(), password.trim())
                } },
                modifier = Modifier
                    .width(133.dp)
                    .height(50.dp),
                border = BorderStroke(width = 2.dp, color = Color(0xFF000000)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0F2FE)), // blue color
                shape = RoundedCornerShape(50),
            ) {
                    Text(
                        text = "LOGIN",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color(0xFF0284C7)
                        )
                    )
//                }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Jika terjadi kendala, silahkan hubungi Admin",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFFA72929),
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                    )
                )

            }
        }
    }
