const gulp = require('gulp');
const babel = require('gulp-babel');
const webpack = require('webpack-stream');


gulp.task('default', () =>
  gulp.src('target/generated-sources/swagger/src/**/*.js')
    .pipe(webpack( require('./target/generated-sources/swagger/webpack.config.js') ))
    .pipe(gulp.dest('target/dist/'))
    .pipe(babel({
      presets: ['@babel/env']
    }))
    .pipe(gulp.dest('target/transpile-output/'))
);