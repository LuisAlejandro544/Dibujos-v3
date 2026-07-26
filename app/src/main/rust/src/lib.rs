// Native Rust Core Module for KidsDraw
// Handles memory-safe stroke simplification using Ramer-Douglas-Peucker algorithm,
// color harmony calculations, and fast vector bounds checking.

use jni::JNIEnv;
use jni::objects::{JClass, JString};
use jni::sys::{jfloatArray, jint};

#[no_mangle]
pub extern "system" fn Java_com_example_nativeengine_NativeDrawingEngine_getRustCoreVersion(
    env: JNIEnv,
    _class: JClass,
) -> JString {
    let output = env.new_string("Rust Core v1.0.0 (Memory Safe Vector Engine)")
        .expect("Couldn't create java string!");
    output.into_raw()
}

// Ramer-Douglas-Peucker point reduction algorithm for path optimization
fn perpendicular_distance(px: f32, py: f32, l1x: f32, l1y: f32, l2x: f32, l2y: f32) -> f32 {
    let dx = l2x - l1x;
    let dy = l2y - l1y;

    if dx == 0.0 && dy == 0.0 {
        return ((px - l1x).powi(2) + (py - l1y).powi(2)).sqrt();
    }

    let num = (dy * px - dx * py + l2x * l1y - l2y * l1x).abs();
    let den = (dx * dx + dy * dy).sqrt();
    num / den
}

pub fn simplify_points_rdp(points: &[(f32, f32)], epsilon: f32) -> Vec<(f32, f32)> {
    if points.len() < 3 {
        return points.to_vec();
    }

    let mut max_dist = 0.0f32;
    let mut index = 0usize;
    let end = points.len() - 1;

    for i in 1..end {
        let dist = perpendicular_distance(
            points[i].0, points[i].1,
            points[0].0, points[0].1,
            points[end].0, points[end].1
        );
        if dist > max_dist {
            max_dist = dist;
            index = i;
        }
    }

    if max_dist > epsilon {
        let mut rec_results1 = simplify_points_rdp(&points[0..=index], epsilon);
        let mut rec_results2 = simplify_points_rdp(&points[index..=end], epsilon);

        rec_results1.pop();
        rec_results1.append(&mut rec_results2);
        rec_results1
    } else {
        vec![points[0], points[end]]
    }
}
